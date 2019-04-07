package ru.appliedtech.chess.playoff;

public class PlayoffLevelSetup {
    private final PlayoffLevel level;
    private final int classicRounds;
    private final int rapidRounds;
    private final int blitzRounds;
    private final boolean armageddon;

    public PlayoffLevelSetup(PlayoffLevel level, int classicRounds, int rapidRounds, int blitzRounds, boolean armageddon) {
        this.level = level;
        this.classicRounds = classicRounds;
        this.rapidRounds = rapidRounds;
        this.blitzRounds = blitzRounds;
        this.armageddon = armageddon;
    }

    public int getBlitzRounds() {
        return blitzRounds;
    }

    public int getClassicRounds() {
        return classicRounds;
    }

    public PlayoffLevel getLevel() {
        return level;
    }

    public int getRapidRounds() {
        return rapidRounds;
    }

    public boolean hasArmageddon() {
        return armageddon;
    }
}
