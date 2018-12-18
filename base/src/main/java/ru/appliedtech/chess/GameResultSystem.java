package ru.appliedtech.chess;

import java.math.BigDecimal;
import java.util.*;

import static java.math.BigDecimal.*;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static ru.appliedtech.chess.GameResultSystem.GameResultName.*;

public class GameResultSystem {
    private final Map<GameResultName, GameResult> possibleResults;

    public GameResultSystem(Map<GameResultName, GameResult> possibleResults) {
        this.possibleResults = new HashMap<>(possibleResults);
    }

    public GameResult getResult(GameResultName gameResultName) {
        GameResult gameResult = possibleResults.get(gameResultName);
        if (gameResult == null) {
            throw new IllegalArgumentException(gameResultName.getName());
        }
        return gameResult;
    }

    public static final GameResultSystem STANDARD = standardSystem();

    private static GameResultSystem standardSystem() {
        Map<GameResultName, GameResult> possibleResults = new HashMap<>();
        possibleResults.put(white_won, new GameResult(white_won, ONE, ZERO));
        possibleResults.put(black_won, new GameResult(black_won, ZERO, ONE));
        possibleResults.put(draw, new GameResult(draw, new BigDecimal(0.5), new BigDecimal(0.5)));
        possibleResults.put(forfeit, new GameResult(forfeit, ZERO, ZERO));
        possibleResults.put(white_forfeit, new GameResult(white_forfeit, ZERO, ONE));
        possibleResults.put(black_forfeit, new GameResult(black_forfeit, ONE, ZERO));
        possibleResults.put(unknown, new GameResult(unknown, ZERO, ZERO));
        return new GameResultSystem(possibleResults);
    }

    public enum GameResultName {
        white_won("white_won", "white won", "white", "ww", "белые победили"),
        black_won("black_won", "black won", "black", "bw", "чёрные победили", "черные победили"),
        draw("draw", "ничья"),
        forfeit("forfeit", "не состоялась", "отменена", "неявка"),
        white_forfeit("white_forfeit", "white forfeit", "неявка белых"),
        black_forfeit("black_forfeit", "black forfeit", "неявка чёрных", "неявка черных"),
        unknown("unknown", "неизвестен", "*", "");

        private final String name;
        private final Set<String> synonyms;

        GameResultName(String name, String... synonyms) {
            this.name = name;
            this.synonyms = new HashSet<>(asList(synonyms));
            this.synonyms.add(name);
        }

        public static GameResultName resolve(final String resultName) {
            return stream(values())
                    .filter(value -> value.synonyms.contains(resultName))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(resultName));
        }

        public String getName() {
            return name;
        }
    }

    public static class GameResult {
        private final GameResultName name;
        private final BigDecimal whiteScore;
        private final BigDecimal blackScore;

        GameResult(GameResultName name, BigDecimal whiteScore, BigDecimal blackScore) {
            this.name = name;
            this.whiteScore = whiteScore;
            this.blackScore = blackScore;
        }

        public BigDecimal getWhiteScore() {
            return whiteScore;
        }

        public BigDecimal getBlackScore() {
            return blackScore;
        }

        public GameResultName getName() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GameResult that = (GameResult) o;
            return name == that.name &&
                    Objects.equals(whiteScore, that.whiteScore) &&
                    Objects.equals(blackScore, that.blackScore);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, whiteScore, blackScore);
        }
    }
}
