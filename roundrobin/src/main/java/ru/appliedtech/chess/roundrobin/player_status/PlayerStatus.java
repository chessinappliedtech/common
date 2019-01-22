package ru.appliedtech.chess.roundrobin.player_status;

import ru.appliedtech.chess.Game;
import ru.appliedtech.chess.Player;
import ru.appliedtech.chess.TournamentDescription;
import ru.appliedtech.chess.elorating.*;
import ru.appliedtech.chess.roundrobin.RoundRobinSetup;
import ru.appliedtech.chess.storage.EloRatingReadOnlyStorage;
import ru.appliedtech.chess.storage.GameStorage;
import ru.appliedtech.chess.storage.KValueReadOnlyStorage;
import ru.appliedtech.chess.storage.PlayerStorage;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

public class PlayerStatus {
    private final Player player;
    private final PlayerStorage playerStorage;
    private final GameStorage gameStorage;
    private final EloRatingReadOnlyStorage eloRatingStorage;
    private final KValueReadOnlyStorage kValueReadOnlyStorage;
    private final TournamentDescription tournamentDescription;
    private final RoundRobinSetup roundRobinSetup;
    private final EloRatingCalculator eloRatingCalculator;
    private List<Opponent> opponents;

    public PlayerStatus(Player player,
                        PlayerStorage playerStorage,
                        GameStorage gameStorage,
                        EloRatingReadOnlyStorage eloRatingStorage,
                        KValueReadOnlyStorage kValueReadOnlyStorage,
                        TournamentDescription tournamentDescription,
                        RoundRobinSetup roundRobinSetup) {
        this.player = player;
        this.playerStorage = playerStorage;
        this.gameStorage = gameStorage;
        this.eloRatingStorage = eloRatingStorage;
        this.kValueReadOnlyStorage = kValueReadOnlyStorage;
        this.tournamentDescription = tournamentDescription;
        this.roundRobinSetup = roundRobinSetup;
        this.eloRatingCalculator = new EloRatingCalculator();
        generate();
    }

    private void generate() {
        List<Player> otherPlayers = tournamentDescription.getPlayers().stream()
                .filter(id -> !id.equals(player.getId()))
                .map(toPlayer())
                .collect(toList());
        List<Game> playerGames = gameStorage.getGames(of(player.getId()));
        opponents = otherPlayers.stream()
                .map(otherPlayer -> new Opponent(otherPlayer.getId(), playerGames.stream().filter(of(otherPlayer.getId())).collect(toList())))
                .collect(toList());
    }

    public Player getPlayer() {
        return player;
    }

    public List<Opponent> getOpponents() {
        return opponents;
    }

    public Player resolvePlayer(String id) {
        return toPlayer().apply(id);
    }

    private Function<String, Player> toPlayer() {
        return id -> playerStorage.getPlayer(id).orElseThrow(() -> new IllegalStateException(MessageFormat.format("Player {0} is not found", id)));
    }

    private Predicate<Game> of(String id) {
        return game -> tournamentDescription.getTournamentId().equals(game.getTournamentId()) && game.isPlayedBy(id);
    }

    public Function<Game, EloRatingChange> toRatingChange(Player player) {
        return game -> {
            boolean isGamePlayed = game.isGamePlayed();
            if (isGamePlayed) {
                EloRating initialRating = getEloRating(player.getId());
                KValueSet kValueSet = kValueReadOnlyStorage.getKValuesSet(player.getId());
                KValue kValue = roundRobinSetup.selectKValue(kValueSet);
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

    public int getGamesPlayed() {
        return (int) getOpponents().stream().mapToLong(o -> o.getGames().stream().filter(Game::isGamePlayed).count()).sum();
    }

    public Optional<BigDecimal> getTotalScore() {
        return getOpponents().stream()
                .flatMap(o -> o.getGames().stream())
                .map(game -> game.getScoreOf(player.getId()))
                .reduce(BigDecimal::add);
    }

    public Optional<EloRatingChange> getRatingChange() {
        return getOpponents().stream()
                .flatMap(o -> o.getGames().stream())
                .map(game -> toRatingChange(player).apply(game))
                .reduce(EloRatingChange::sum);
    }

    public EloRating getCurrentRating() {
        return getEloRating(player.getId());
    }
}
