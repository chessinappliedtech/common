package ru.appliedtech.chess.tiebreaksystems;

import ru.appliedtech.chess.Player;

import java.util.Comparator;

public interface TieBreakSystem extends Comparator<Player> {
    @Override
    int compare(Player p1, Player p2);

    String getName();
}
