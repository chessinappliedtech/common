package ru.appliedtech.chess.roundrobin.color_allocating;

import ru.appliedtech.chess.Color;

public interface ColorAllocatingSystem {

    Color getColor(String playerId, String opponentId, int gameNumber);
}
