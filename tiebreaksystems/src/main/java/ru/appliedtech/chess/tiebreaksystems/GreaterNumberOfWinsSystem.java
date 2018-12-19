package ru.appliedtech.chess.tiebreaksystems;

import ru.appliedtech.chess.Game;
import ru.appliedtech.chess.Player;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public final class GreaterNumberOfWinsSystem implements ScoringTieBreakSystem {
    private final List<Game> games;

    public GreaterNumberOfWinsSystem(List<Game> games) {
        this.games = new ArrayList<>(games);
    }

    @Override
    public BigDecimal scoreOf(Player player) {
        return new BigDecimal(
            games.stream()
                .filter(game -> game.isPlayedBy(player.getId()))
                .mapToInt(game -> game.hasWon(player.getId()) ? 1 : 0)
                .sum());
    }

    @Override
    public String getName() {
        return "number-of-wins";
    }
}
