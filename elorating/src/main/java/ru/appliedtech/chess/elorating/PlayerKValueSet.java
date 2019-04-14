package ru.appliedtech.chess.elorating;

import java.text.MessageFormat;

public class PlayerKValueSet {
    private final String playerId;
    private final KValueSet kValueSet;

    public PlayerKValueSet(String playerId, KValueSet kValueSet) {
        this.playerId = playerId;
        this.kValueSet = kValueSet;
    }

    public String getPlayerId() {
        return playerId;
    }

    public KValueSet getKValueSet() {
        return kValueSet;
    }

    @Override
    public String toString() {
        return MessageFormat.format("{0} {1}", playerId, kValueSet);
    }
}
