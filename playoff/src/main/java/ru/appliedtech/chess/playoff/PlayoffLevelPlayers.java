package ru.appliedtech.chess.playoff;

public class PlayoffLevelPlayers {
    private final PlayoffLevel level;
    private final String playerId1;
    private final String playerId2;

    public PlayoffLevelPlayers(PlayoffLevel level, String playerId1, String playerId2) {
        this.level = level;
        this.playerId1 = playerId1;
        this.playerId2 = playerId2;
    }

    public PlayoffLevel getLevel() {
        return level;
    }

    public String getPlayerId1() {
        return playerId1;
    }

    public String getPlayerId2() {
        return playerId2;
    }
}
