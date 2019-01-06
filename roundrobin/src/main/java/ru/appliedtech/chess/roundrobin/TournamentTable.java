package ru.appliedtech.chess.roundrobin;

import ru.appliedtech.chess.Game;
import ru.appliedtech.chess.Player;
import ru.appliedtech.chess.elorating.*;
import ru.appliedtech.chess.storage.EloRatingReadOnlyStorage;
import ru.appliedtech.chess.storage.GameStorage;
import ru.appliedtech.chess.storage.KValueReadOnlyStorage;
import ru.appliedtech.chess.storage.PlayerStorage;
import ru.appliedtech.chess.tiebreaksystems.ScoringTieBreakSystem;
import ru.appliedtech.chess.tiebreaksystems.TieBreakSystem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

public final class TournamentTable {

    public static final Comparator<Player> PLAYER_ORDER_COMPARATOR =
            comparing(Player::getLastName)
                .thenComparing(Player::getFirstName)
                .thenComparing(Player::getId);

    private final PlayerStorage playerStorage;
    private final GameStorage gameStorage;
    private final EloRatingReadOnlyStorage eloRatingStorage;
    private final KValueReadOnlyStorage kValueReadOnlyStorage;
    private final RoundRobinSetup roundRobinSetup;
    private List<PlayerRow> playerRows;

    public TournamentTable(PlayerStorage playerStorage,
                           GameStorage gameStorage,
                           EloRatingReadOnlyStorage eloRatingStorage,
                           KValueReadOnlyStorage kValueReadOnlyStorage,
                           RoundRobinSetup roundRobinSetup) {
        this.playerStorage = playerStorage;
        this.gameStorage = gameStorage;
        this.eloRatingStorage = eloRatingStorage;
        this.kValueReadOnlyStorage = kValueReadOnlyStorage;
        this.roundRobinSetup = roundRobinSetup;
        generate();
    }

    private void generate() {
        playerRows = playerStorage.getPlayers().stream()
                .sorted(PLAYER_ORDER_COMPARATOR)
                .map(toPlayerRow())
                .collect(toList());
    }

    private Function<Player, PlayerRow> toPlayerRow() {
        RoundRobinTieBreakSystemFactory roundRobinTieBreakSystemFactory = new RoundRobinTieBreakSystemFactory(
                playerStorage.getPlayers(), gameStorage.getGames(), roundRobinSetup);
        List<TieBreakSystem> tieBreakSystems = roundRobinSetup.getTieBreakSystems().stream()
                .map(roundRobinTieBreakSystemFactory::create)
                .collect(toList());
        EloRatingCalculator eloRatingCalculator = new EloRatingCalculator();
        return player -> {
            EloRating initialRating = getEloRating(player.getId());
            KValueSet kValueSet = kValueReadOnlyStorage.getKValuesSet(player.getId());
            KValue kValue = roundRobinSetup.selectKValue(kValueSet);
            List<Game> playerGames = gameStorage.getGames(game -> game.isPlayedBy(player.getId()));
            List<EloRatingChange> eloRatingChanges = playerGames.stream()
                    .map(toRatingChange(eloRatingCalculator, player, initialRating, kValue))
                    .collect(toList());
            EloRating currentRating = initialRating.sum(
                    eloRatingChanges.stream().reduce(EloRatingChange.ZERO, EloRatingChange::sum));
            int gamesPlayed = playerGames.size();
            List<TieBreakValue> tieBreakValues = tieBreakSystems.stream()
                    .filter(ScoringTieBreakSystem.class::isInstance)
                    .map(ScoringTieBreakSystem.class::cast)
                    .map(tbs -> new TieBreakValue(tbs, tbs.scoreOf(player)))
                    .collect(toList());
            List<OpponentCell> opponents = playerStorage.getPlayers(p -> !p.getId().equals(player.getId())).stream()
                    .map(toOpponentCell(player))
                    .collect(toList());
            return new PlayerRow(initialRating, currentRating, gamesPlayed, tieBreakValues, opponents);
        };
    }

    private Function<Player, OpponentCell> toOpponentCell(Player player) {
        return opponent -> {
            List<BigDecimal> scores = gameStorage.getGames(player.getId(), opponent.getId()).stream()
                    .map(game -> game.getScoreOf(player.getId()))
                    .collect(toList());
            return new OpponentCell(opponent.getId(), scores);
        };
    }

    private Function<Game, EloRatingChange> toRatingChange(EloRatingCalculator eloRatingCalculator,
                                                           Player player,
                                                           EloRating initialRating,
                                                           KValue kValue) {
        return game -> {
            BigDecimal score = game.getScoreOf(player.getId());
            EloRating opponentRating = getEloRating(game.getOpponentOf(player.getId()));
            return eloRatingCalculator.ratingChange(initialRating, kValue, opponentRating, score);
        };
    }

    private EloRating getEloRating(String playerId) {
        return eloRatingStorage
                .ratingOf(playerId)
                .orElse(new EloRating(roundRobinSetup.getDefaultInitialEloRating()));
    }

    private static class PlayerRow {
        private final EloRating initialRating;
        private final EloRating currentRating;
        private final int gamesPlayed;
        private final List<TieBreakValue> tieBreakValues;
        private final List<OpponentCell> opponents;

        private PlayerRow(EloRating initialRating,
                          EloRating currentRating,
                          int gamesPlayed,
                          List<TieBreakValue> tieBreakValues,
                          List<OpponentCell> opponents) {
            this.initialRating = initialRating;
            this.currentRating = currentRating;
            this.gamesPlayed = gamesPlayed;
            this.tieBreakValues = tieBreakValues != null ? new ArrayList<>(tieBreakValues) : emptyList();
            this.opponents = opponents != null ? new ArrayList<>(opponents) : emptyList();
        }

        public int getGamesPlayed() {
            return gamesPlayed;
        }

        public EloRating getCurrentRating() {
            return currentRating;
        }

        public EloRating getInitialRating() {
            return initialRating;
        }

        public List<OpponentCell> getOpponents() {
            return unmodifiableList(opponents);
        }

        public List<TieBreakValue> getTieBreakValues() {
            return unmodifiableList(tieBreakValues);
        }
    }

    private static class OpponentCell {
        private final String opponentId;
        private final List<BigDecimal> scores;

        private OpponentCell(String opponentId, List<BigDecimal> scores) {
            this.opponentId = opponentId;
            this.scores = scores != null ? new ArrayList<>(scores) : emptyList();
        }

        public List<BigDecimal> getScores() {
            return unmodifiableList(scores);
        }

        public String getOpponentId() {
            return opponentId;
        }
    }

    private static class TieBreakValue {
        private final TieBreakSystem tieBreakSystem;
        private final BigDecimal value;

        private TieBreakValue(TieBreakSystem tieBreakSystem, BigDecimal value) {
            this.tieBreakSystem = tieBreakSystem;
            this.value = value;
        }

        public BigDecimal getValue() {
            return value;
        }

        public TieBreakSystem getTieBreakSystem() {
            return tieBreakSystem;
        }
    }
}
