package ru.appliedtech.chess;

import static java.util.Arrays.stream;

public enum TimeControlType {
    CLASSIC,
    RAPID,
    BLITZ,
    ARMAGEDDON;

    public static TimeControlType resolve(final String type) {
        return stream(values())
                .filter(value -> value.name().contains(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(type));
    }
}
