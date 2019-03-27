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
import java.util.*;
import java.util.function.Function;

import static java.util.Collections.*;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

public final class TournamentTable {

    private final PlayerStorage playerStorage;
    private final GameStorage gameStorage;
    private final EloRatingReadOnlyStorage eloRatingStorage;
    private final KValueReadOnlyStorage kValueReadOnlyStorage;
    private final RoundRobinSetup roundRobinSetup;
    private final List<String> registeredPlayers;
    private final List<String> quitPlayers;
    private List<PlayerRow> playerRows;
    private List<TieBreakSystem> tieBreakSystems;
    private Map<String, Integer> ranking;

    public TournamentTable(PlayerStorage playerStorage,
                           GameStorage gameStorage,
                           EloRatingReadOnlyStorage eloRatingStorage,
                           KValueReadOnlyStorage kValueReadOnlyStorage,
                           RoundRobinSetup roundRobinSetup,
                           List<String> registeredPlayers,
                           List<String> quitPlayers) {
        this.playerStorage = playerStorage;
        this.gameStorage = gameStorage;
        this.eloRatingStorage = eloRatingStorage;
        this.kValueReadOnlyStorage = kValueReadOnlyStorage;
        this.roundRobinSetup = roundRobinSetup;
        this.registeredPlayers = registeredPlayers;
        this.quitPlayers = quitPlayers;
        generate();
    }

    private void generate() {
        List<Player> players = playerStorage.getPlayers()
                .stream()
                .filter(player -> registeredPlayers.contains(player.getId()))
                .collect(toList());
        RoundRobinTieBreakSystemFactory roundRobinTieBreakSystemFactory = new RoundRobinTieBreakSystemFactory(
                players, gameStorage.getGames(), roundRobinSetup);
        tieBreakSystems = roundRobinSetup.getTieBreakSystems().stream()
                .map(roundRobinTieBreakSystemFactory::create)
                .collect(toList());
        playerRows = players.stream()
                .map(toPlayerRow())
                .collect(toList());
        ranking = evaluateRanking(playerRows);
    }

    private static Map<String, Integer> evaluateRanking(List<PlayerRow> playerRows) {
        Comparator<List<TieBreakValue>> tieBreaksComparator = new TieBreaksComparator().reversed();
        List<PlayerRow> rankedRows = playerRows.stream()
                .filter(playerRow -> !playerRow.isQuit())
                .sorted(comparing(PlayerRow::getTieBreakValues, tieBreaksComparator))
                .collect(toList());
        Map<String, Integer> result = new HashMap<>();
        int rank = 1;
        PlayerRow previousPlayerRow = null;
        for (int i = 0; i < rankedRows.size(); i++) {
            PlayerRow playerRow = rankedRows.get(i);
            if (i == 0) {
                previousPlayerRow = playerRow;
                result.put(previousPlayerRow.getPlayer().getId(), rank);
            } else {
                if (tieBreaksComparator.compare(previousPlayerRow.getTieBreakValues(), playerRow.getTieBreakValues()) == 0) {
                    result.put(playerRow.getPlayer().getId(), rank);
                } else {
                    result.put(playerRow.getPlayer().getId(), ++rank);
                }
                previousPlayerRow = playerRow;
            }
        }
        return result;
    }

    public int getPlayersCount() {
        return playerRows.size();
    }

    public List<PlayerRow> getPlayerRows() {
        return unmodifiableList(playerRows);
    }

    public Map<String, Integer> getRanking() {
        return unmodifiableMap(ranking);
    }

    public List<TieBreakSystem> getTieBreakSystems() {
        return tieBreakSystems;
    }

    public RoundRobinSetup getRoundRobinSetup() {
        return roundRobinSetup;
    }

    private Function<Player, PlayerRow> toPlayerRow() {
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
            int gamesPlayed = (int) playerGames.stream().filter(Game::isGamePlayed).count();
            List<TieBreakValue> tieBreakValues = tieBreakSystems.stream()
                    .filter(ScoringTieBreakSystem.class::isInstance)
                    .map(ScoringTieBreakSystem.class::cast)
                    .map(tbs -> new TieBreakValue(tbs, tbs.scoreOf(player)))
                    .collect(toList());
            List<OpponentCell> opponents = playerStorage.getPlayers(p -> !p.getId().equals(player.getId())).stream()
                    .map(toOpponentCell(player))
                    .collect(toList());
            boolean isQuit = quitPlayers.contains(player.getId());
            return new PlayerRow(player, initialRating, currentRating, gamesPlayed, tieBreakValues, opponents, isQuit);
        };
    }

    private Function<Player, OpponentCell> toOpponentCell(Player player) {
        return opponent -> {
            List<BigDecimal> scores = gameStorage.getGames(player.getId(), opponent.getId()).stream()
                    .map(game -> game.getScoreOf(player.getId()))
                    .collect(toList());
            boolean isQuit = quitPlayers.contains(opponent.getId());
            return new OpponentCell(opponent.getId(), scores, isQuit);
        };
    }

    private Function<Game, EloRatingChange> toRatingChange(EloRatingCalculator eloRatingCalculator,
                                                           Player player,
                                                           EloRating initialRating,
                                                           KValue kValue) {
        return game -> {
            boolean isGamePlayed = game.isGamePlayed();
            if (isGamePlayed) {
                BigDecimal score = game.getScoreOf(player.getId());
                EloRating opponentRating = getEloRating(game.getOpponentOf(player.getId()));
                return eloRatingCalculator.ratingChange(initialRating, kValue, opponentRating, score);
            } else {
                return new EloRatingChange(BigDecimal.ZERO);
            }
        };
    }

    private EloRating getEloRating(String playerId) {
        return eloRatingStorage
                .ratingOf(playerId)
                .orElse(new EloRating(roundRobinSetup.getDefaultInitialEloRating()));
    }

    public static class PlayerRow {
        private final Player player;
        private final EloRating initialRating;
        private final EloRating currentRating;
        private final int gamesPlayed;
        private final List<TieBreakValue> tieBreakValues;
        private final List<OpponentCell> opponents;
        private final boolean isQuit;

        private PlayerRow(Player player,
                          EloRating initialRating,
                          EloRating currentRating,
                          int gamesPlayed,
                          List<TieBreakValue> tieBreakValues,
                          List<OpponentCell> opponents,
                          boolean isQuit) {
            this.player = player;
            this.initialRating = initialRating;
            this.currentRating = currentRating;
            this.gamesPlayed = gamesPlayed;
            this.tieBreakValues = tieBreakValues != null ? new ArrayList<>(tieBreakValues) : emptyList();
            this.opponents = opponents != null ? new ArrayList<>(opponents) : emptyList();
            this.isQuit = isQuit;
        }

        public Player getPlayer() {
            return player;
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

        public boolean isQuit() {
            return isQuit;
        }

        @Override
        public String toString() {
            return Objects.toString(player);
        }
    }

    public static class OpponentCell {
        private final String opponentId;
        private final List<BigDecimal> scores;
        private final boolean isQuit;

        private OpponentCell(String opponentId, List<BigDecimal> scores, boolean isQuit) {
            this.opponentId = opponentId;
            this.scores = scores != null ? new ArrayList<>(scores) : emptyList();
            this.isQuit = isQuit;
        }

        public List<BigDecimal> getScores() {
            return unmodifiableList(scores);
        }

        public String getOpponentId() {
            return opponentId;
        }

        public boolean isQuit() {
            return isQuit;
        }

        @Override
        public String toString() {
            return opponentId;
        }
    }

    public static class TieBreakValue {
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

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    private static class TieBreaksComparator implements Comparator<List<TieBreakValue>> {
        @Override
        public int compare(List<TieBreakValue> o1, List<TieBreakValue> o2) {
            for (int i = 0; i < o1.size(); i++) {
                int tbsCompareResult = o1.get(i).getValue().compareTo(o2.get(i).getValue());
                if (tbsCompareResult != 0) {
                    return tbsCompareResult;
                }
            }
            return 0;
        }
    }
}
