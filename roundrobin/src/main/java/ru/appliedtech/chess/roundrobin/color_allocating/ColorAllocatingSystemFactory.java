package ru.appliedtech.chess.roundrobin.color_allocating;

import ru.appliedtech.chess.roundrobin.RoundRobinSetup;

import java.util.List;

public class ColorAllocatingSystemFactory {
    private final RoundRobinSetup roundRobinSetup;

    public ColorAllocatingSystemFactory(RoundRobinSetup roundRobinSetup) {
        this.roundRobinSetup = roundRobinSetup;
    }

    public ColorAllocatingSystem create(List<String> initialPlayers, List<String> joinedPlayers) {
        int maxGames = roundRobinSetup.getRoundsAmount();
        String systemName = roundRobinSetup.getColorAllocatingSystem();
        if ("fixed-alternation-color-allocating-system".equals(systemName)) {
            long seed = roundRobinSetup.getColorAllocatingSystemSeed();
            return new FixedAlternationColorAllocatingSystem(seed, initialPlayers, joinedPlayers, maxGames);
        }
        throw new IllegalArgumentException(systemName);
    }
}
