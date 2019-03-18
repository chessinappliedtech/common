package ru.appliedtech.chess.roundrobin.color_allocating;

public interface ColorAllocatingSystem {
    enum Color {
        white,
        black;

        Color revert() {
            return white == this ? black : white;
        }
    }

    Color getColor(String playerId, String opponentId, int gameNumber);
}
