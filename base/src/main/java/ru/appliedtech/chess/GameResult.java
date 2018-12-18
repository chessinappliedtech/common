package ru.appliedtech.chess;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;

public enum GameResult {
    white_won("white_won", "white", "ww", "белые победили"),
    black_won("black_won", "black", "bw", "чёрные победили", "черные победили"),
    draw("draw", "ничья"),
    forfeit("forfeit", "не состоялась", "отменена", "неявка"),
    white_forfeit("white forfeit", "неявка белых"),
    black_forfeit("black forfeit", "неявка чёрных", "неявка черных"),
    unknown("unknown", "неизвестен", "*", "");

    private final String name;
    private final List<String> synonyms;

    GameResult(String... synonyms) {
        this.name = synonyms[0];
        this.synonyms = asList(synonyms);
    }

    public static GameResult resolve(final String result) {
        return stream(values())
                .filter(value -> value.synonyms.contains(result))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(result));
    }

    public String getName() {
        return name;
    }
}
