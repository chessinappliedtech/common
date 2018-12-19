package ru.appliedtech.chess.tiebreaksystems;

import ru.appliedtech.chess.Game;
import ru.appliedtech.chess.Player;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public final class KoyaSystem implements ScoringTieBreakSystem {
    private final List<Player> players;
    private final List<Game> games;
    private final BigDecimal maxScore;
    private final int threshold;

    public KoyaSystem(List<Player> players, List<Game> games, BigDecimal maxScore, int threshold) {
        this.players = new ArrayList<>(players);
        this.games = new ArrayList<>(games);
        this.maxScore = maxScore;
        this.threshold = threshold;
    }

    @Override
    public BigDecimal scoreOf(Player player) {
        return players.stream()
                .filter(without(player))
                .filter(havingScoreAbove(maxScore.multiply(new BigDecimal(threshold / 100.0))))
                .map(toScoreOf(player))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Predicate<Player> without(Player player) {
        return p -> !p.getId().equals(player.getId());
    }

    private Function<Player, BigDecimal> toScoreOf(Player player) {
        return opponent -> games.stream()
                .filter(game -> game.isPlayedBy(player.getId()) && game.isPlayedBy(opponent.getId()))
                .map(game -> game.getScoreOf(player.getId()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Predicate<Player> havingScoreAbove(BigDecimal thresholdScore) {
        return player -> {
            BigDecimal playerScore = games.stream()
                    .filter(game -> game.isPlayedBy(player.getId()))
                    .map(game -> game.getScoreOf(player.getId()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            return playerScore.compareTo(thresholdScore) >= 0;
        };
    }

    @Override
    public String getName() {
        return "koya";
    }
}
