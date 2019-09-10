package ru.appliedtech.chess.roundrobin.color_allocating;

import ru.appliedtech.chess.Color;

import java.text.MessageFormat;
import java.util.*;

import static java.util.stream.Collectors.toList;

public class FixedAlternationColorAllocatingSystem implements ColorAllocatingSystem {
    private final int maxGames;
    private final List<Pair> pairs;

    FixedAlternationColorAllocatingSystem(long seed, List<String> initialPlayers,
                                          List<String> joinedPlayers,
                                          int maxGames) {
        this.maxGames = maxGames;
        this.pairs = allocate(seed, initialPlayers, joinedPlayers, maxGames);
    }

    private static List<Pair> allocate(long seed, List<String> initialPlayers,
                                       List<String> joinedPlayers,
                                       int maxGames) {
        List<Pair> pairs = new ArrayList<>();
        Random random = new Random(seed);
        boolean isWhite = random.nextBoolean();
        boolean evenNumberOfPlayers = initialPlayers.size() % 2 == 0;
        Map<String, Counts> colorCounts = new HashMap<>();
        for (String playerId : initialPlayers) {
            for (String opponentId : initialPlayers) {
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
                storeColorCounts(colorCounts, playerId, colors);
            }
            if (!evenNumberOfPlayers) {
                isWhite = !isWhite;
            }
        }
        if (!evenNumberOfPlayers) {
            isWhite = random.nextBoolean();
            for (String playerId : initialPlayers) {
                for (String joinedPlayerId : joinedPlayers) {
                    List<Color> colors = new ArrayList<>();
                    for (int i = 0; i < maxGames; i++) {
                        colors.add(isWhite ? Color.white : Color.black);
                        isWhite = !isWhite;
                    }
                    Pair pair = new Pair(playerId, joinedPlayerId, colors);
                    pairs.add(pair);
                    storeColorCounts(colorCounts, playerId, colors);
                    Pair pair2 = new Pair(joinedPlayerId, playerId, colors.stream().map(Color::revert).collect(toList()));
                    pairs.add(pair2);
                }
                if (joinedPlayers.size() % 2 == 0) {
                    isWhite = !isWhite;
                }
            }
            for (String joinedPlayer1 : joinedPlayers) {
                for (String joinedPlayer2 : joinedPlayers) {
                    List<Color> colors = new ArrayList<>();
                    for (int i = 0; i < maxGames; i++) {
                        colors.add(isWhite ? Color.white : Color.black);
                        isWhite = !isWhite;
                    }
                    Pair pair = new Pair(joinedPlayer1, joinedPlayer2, colors);
                    pairs.add(pair);
                    storeColorCounts(colorCounts, joinedPlayer1, colors);
                    Pair pair2 = new Pair(joinedPlayer2, joinedPlayer1, colors.stream().map(Color::revert).collect(toList()));
                    pairs.add(pair2);
                }
                if (joinedPlayers.size() % 2 == 0) {
                    isWhite = !isWhite;
                }
            }
        } else {
            for (String playerId : initialPlayers) {
                isWhite = colorCounts.get(playerId).isMoreBlacks();
                for (String joinedPlayerId : joinedPlayers) {
                    List<Color> colors = new ArrayList<>();
                    for (int i = 0; i < maxGames; i++) {
                        colors.add(isWhite ? Color.white : Color.black);
                        isWhite = !isWhite;
                    }
                    Pair pair = new Pair(playerId, joinedPlayerId, colors);
                    pairs.add(pair);
                    storeColorCounts(colorCounts, playerId, colors);
                    Pair pair2 = new Pair(joinedPlayerId, playerId, colors.stream().map(Color::revert).collect(toList()));
                    pairs.add(pair2);
                }
            }
            for (String joinedPlayer1 : joinedPlayers) {
                isWhite = colorCounts.get(joinedPlayer1).isMoreBlacks();
                for (String joinedPlayer2 : joinedPlayers) {
                    List<Color> colors = new ArrayList<>();
                    for (int i = 0; i < maxGames; i++) {
                        colors.add(isWhite ? Color.white : Color.black);
                        isWhite = !isWhite;
                    }
                    Pair pair = new Pair(joinedPlayer1, joinedPlayer2, colors);
                    pairs.add(pair);
                    storeColorCounts(colorCounts, joinedPlayer1, colors);
                    Pair pair2 = new Pair(joinedPlayer2, joinedPlayer1, colors.stream().map(Color::revert).collect(toList()));
                    pairs.add(pair2);
                }
            }
        }
        return pairs;
    }

    private static void storeColorCounts(Map<String, Counts> colorCounts, String playerId, List<Color> colors) {
        colorCounts.compute(playerId, (key, value) -> {
            if (value == null) {
                value = new Counts(
                        colors.stream().filter(c -> c == Color.white).count(),
                        colors.stream().filter(c -> c == Color.black).count());
            } else {
                value = value.increase(
                        colors.stream().filter(c -> c == Color.white).count(),
                        colors.stream().filter(c -> c == Color.black).count());
            }
            return value;
        });
    }

    @Override
    public Color getColor(String playerId, String opponentId, int gameNumber) {
        checkInput(playerId, opponentId, gameNumber);
        for (Pair pair : pairs) {
            if (pair.match(playerId, opponentId)) {
                return Optional.of(pair)
                        .map(p -> p.getColor(gameNumber))
                        .orElseThrow(IllegalStateException::new);
            }
        }
        return Optional.<Pair>empty()
                .map(p -> p.getColor(gameNumber))
                .orElseThrow(IllegalStateException::new);
    }

    private final static class Counts {
        private long whites;
        private long blacks;

        Counts(long whites, long blacks) {
            this.whites = whites;
            this.blacks = blacks;
        }

        long getBlacks() {
            return blacks;
        }

        long getWhites() {
            return whites;
        }

        Counts increase(long whites, long blacks) {
            return new Counts(this.whites + whites, this.blacks + blacks);
        }

        boolean isMoreWhites() {
            return whites > blacks;
        }

        boolean isMoreBlacks() {
            return blacks > whites;
        }

        boolean isEven() {
            return whites == blacks;
        }
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

        @Override
        public String toString() {
            return MessageFormat.format("{0}->{1}: {2}", playerId, opponentId, colors);
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
