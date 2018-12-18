package ru.appliedtech.chess.tiebreaksystems;

import ru.appliedtech.chess.Game;
import ru.appliedtech.chess.Player;

import java.math.BigDecimal;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class BergerScore {
    private final List<Player> players;
    private final List<Game> games;

    public BergerScore(List<Player> players, List<Game> games) {
        this.players = players;
        this.games = games;
    }

    public BigDecimal scoreOf(Player player) {
        List<String> opponentIds = games.stream()
                .filter(game -> game.isPlayedBy(player.getId()))
                .map(game -> game.getOpponentOf(player.getId()))
                .collect(toList());
        List<Integer> counts = opponentIds.stream()
                .map(opponentId -> games.stream().filter(game -> game.isPlayedBy(player.getId()) && game.isPlayedBy(opponentId)).collect(toList()))
                .map(List::size)
                .collect(toList());
        List<Integer> scores = opponentIds.stream()
                .map(opponentId -> games.stream().filter(game -> game.isPlayedBy(player.getId()) && game.isPlayedBy(opponentId)).collect(toList()))
                .map(gamesWithOpponent -> {
                    List<Integer> scoreWithOneOpponent = gamesWithOpponent.stream().map(game -> game.getScoreOf(player.getId())).collect(toList());
                    if (scoreWithOneOpponent.isEmpty()) {
                        return 0;
                    } else {
                        return scoreWithOneOpponent.stream().mapToInt(Integer::intValue).sum();
                    }
                })
                .collect(toList());
        BigDecimal result = new BigDecimal(0);
        for (int i = 0; i < opponentIds.size(); i++) {
            Integer count = counts.get(i);
            if (count % 2 == 0) {
                Integer score = scores.get(i);
                if (score * 2 > count) {
                    result = result.add(new BigDecimal(score));
                } else if (score * 2 == count) {
                    result = result.add(new BigDecimal(score).divide(new BigDecimal(2)));
                }
            }
        }
        return result;
    }
}
