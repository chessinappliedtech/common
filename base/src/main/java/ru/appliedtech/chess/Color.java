package ru.appliedtech.chess;

public enum Color {
    white,
    black;

    public Color revert() {
        return white == this ? black : white;
    }
}
