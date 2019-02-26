package ru.appliedtech.chess.roundrobin.color_allocating;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class FixedAlternationColorAllocatingSystem implements ColorAllocatingSystem {
    private final int maxGames;
    private final List<Pair> pairs;

    public FixedAlternationColorAllocatingSystem(long seed, List<String> players, int maxGames) {
        this.maxGames = maxGames;
        this.pairs = allocate(seed, players, maxGames);
    }

    private List<Pair> allocate(long seed, List<String> players, int maxGames) {
        List<Pair> pairs = new ArrayList<>();
        Random random = new Random(seed);
        boolean isWhite = random.nextBoolean();
        for (String playerId : players) {
            for (String opponentId : players) {
                if (playerId.equals(opponentId)) {
                    continue;
                }
                List<Color> colors = new ArrayList<>();
                for (int i = 0; i < maxGames; i++) {
                    colors.add(isWhite ? Color.white : Color.black);
                    isWhite = !isWhite;
                }
                Pair pair = new Pair(playerId, opponentId, colors);
                pairs.add(pair);
            }
        }
        return pairs;
    }

    @Override
    public Color getColor(String playerId, String opponentId, int gameNumber) {
        checkInput(playerId, opponentId, gameNumber);
        return pairs.stream()
                .filter(p -> p.match(playerId, opponentId))
                .findFirst()
                .map(p -> p.getColor(gameNumber))
                .orElseThrow(IllegalStateException::new);
    }

    private final static class Pair {
        private final String playerId;
        private final String opponentId;
        private final List<Color> colors;

        private Pair(String playerId, String opponentId, List<Color> colors) {
            this.playerId = playerId;
            this.opponentId = opponentId;
            this.colors = colors;
        }

        Color getColor(int gameNumber) {
            return colors.get(gameNumber - 1);
        }

        boolean match(String playerId, String opponentId) {
            return this.playerId.equals(playerId)
                    && this.opponentId.equals(opponentId);
        }
    }

    private void checkInput(String playerId, String opponentId, int gameNumber) {
        Objects.requireNonNull(playerId);
        Objects.requireNonNull(opponentId);
        if (Objects.equals(playerId, opponentId)) {
            throw new IllegalArgumentException("playerId: " + playerId + ", opponentId: " + opponentId);
        }
        if (gameNumber < 1 || gameNumber > maxGames) {
            throw new IllegalArgumentException("gameNumber is out of range: 1 - " + maxGames);
        }
    }
}
