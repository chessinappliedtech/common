package ru.appliedtech.chess.tiebreaksystems;

import ru.appliedtech.chess.Game;
import ru.appliedtech.chess.Player;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;
import static ru.appliedtech.chess.GameResultSystem.GameResultName.*;

public class BergerScoreSystem {
    public static final BigDecimal TWO = new BigDecimal(2);
    private final List<Game> games;

    public BergerScoreSystem(List<Game> games) {
        this.games = new ArrayList<>(games);
    }

    public BigDecimal scoreOf(Player player) {
        String playerId = player.getId();
        List<String> opponentIds = games.stream()
                .filter(game -> game.isPlayedBy(playerId))
                .map(game -> game.getOpponentOf(playerId))
                .collect(toList());
        return opponentIds.stream()
                .map(opponent -> {
                    BigDecimal opponentMultiplier = toGamesWith(playerId).andThen(toScoreMultiplier(playerId)).apply(opponent);
                    BigDecimal opponentScore = toTotalScoreAt(games).apply(opponent);
                    return opponentMultiplier.multiply(opponentScore);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Function<List<Game>, BigDecimal> toScoreMultiplier(String playerId) {
        return games -> games.stream()
                .map(toOneGameMark(playerId))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static Function<Game, BigDecimal> toOneGameMark(String playerId) {
        return game -> {
            if (game.getResult().getName().equals(draw)) {
                return DRAW_MARK;
            } else if (game.hasWon(playerId)) {
                return WIN_MARK;
            }
            return LOSE_MARK;
        };
    }

    private static BigDecimal WIN_MARK = new BigDecimal(1);
    private static BigDecimal DRAW_MARK = new BigDecimal(0);
    private static BigDecimal LOSE_MARK = new BigDecimal(-1);

    private Function<String, BigDecimal> toTotalScoreAt(List<Game> games) {
        return playerId -> games.stream()
                .filter(game -> game.isPlayedBy(playerId))
                .map(game -> game.getScoreOf(playerId))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Function<String, List<Game>> toGamesWith(String playerId) {
        return opponentId -> games.stream().filter(byPlayers(playerId, opponentId)).collect(toList());
    }

    private Predicate<Game> byPlayers(String player1, String player2) {
        return game -> game.isPlayedBy(player1) && game.isPlayedBy(player2);
    }
}
