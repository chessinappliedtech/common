package ru.appliedtech.chess.storage;

import java.time.Instant;

public class EloRatingKey {
    private final Instant instant;
    private final String playerId;

    public EloRatingKey(Instant instant, String playerId) {
        this.instant = instant;
        this.playerId = playerId;
    }

    public Instant getInstant() {
        return instant;
    }

    public String getPlayerId() {
        return playerId;
    }
}
