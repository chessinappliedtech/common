package ru.appliedtech.chess.tiebreaksystems;

import ru.appliedtech.chess.Player;

import java.math.BigDecimal;

public interface ScoringTieBreakSystem extends TieBreakSystem {
    BigDecimal scoreOf(Player player);

    @Override
    default int compare(Player p1, Player p2) {
        return scoreOf(p2).compareTo(scoreOf(p1));
    }
}
