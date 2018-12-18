package ru.appliedtech.chess;

import java.math.BigDecimal;
import java.util.Map;

import static ru.appliedtech.chess.GameResultSystem.*;
import static ru.appliedtech.chess.GameResultSystem.GameResultName.*;

public final class Game {
    private final String id;
    private final String whiteId;
    private final String blackId;
    private final String date;
    private final GameResult result;
    private final String pgnLocation;
    private final Map<String, Object> outerServiceLinks;

    public Game(String id, String whiteId,
                String blackId,
                String date,
                GameResult result,
                String pgnLocation,
                Map<String, Object> outerServiceLinks) {
        this.id = id;
        this.whiteId = whiteId;
        this.blackId = blackId;
        this.date = date;
        this.result = result;
        this.pgnLocation = pgnLocation;
        this.outerServiceLinks = outerServiceLinks;
    }

    public String getId() {
        return id;
    }

    public GameResult getResult() {
        return result;
    }

    public String getBlackId() {
        return blackId;
    }

    public String getDate() {
        return date;
    }

    public String getWhiteId() {
        return whiteId;
    }

    public boolean isWhite(String id) {
        return getWhiteId().equals(id);
    }

    public boolean isBlack(String id) {
        return getBlackId().equals(id);
    }

    public boolean hasWon(String id) {
        if (isWhite(id)) {
            return result.getName().equals(white_won)
                    || result.getName().equals(black_forfeit);
        } else if (isBlack(id)) {
            return result.getName().equals(black_won)
                    || result.getName().equals(white_forfeit);
        }
        throw new IllegalArgumentException(id);
    }

    public String getPgnLocation() {
        return pgnLocation;
    }

    public Map<String, Object> getOuterServiceLinks() {
        return outerServiceLinks;
    }

    public String getOpponentOf(String id) {
        if (getWhiteId().equals(id)) {
            return getBlackId();
        } else if (getBlackId().equals(id)) {
            return getWhiteId();
        }
        throw new IllegalArgumentException(id);
    }

    public BigDecimal getScoreOf(String id) {
        if (getWhiteId().equals(id)) {
            return result.getWhiteScore();
        } else if (getBlackId().equals(id)) {
            return result.getBlackScore();
        }
        throw new IllegalArgumentException(id);
    }

    public boolean isPlayedBy(String playerId) {
        return getWhiteId().equals(playerId) || getBlackId().equals(playerId);
    }
}
