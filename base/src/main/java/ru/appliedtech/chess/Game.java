package ru.appliedtech.chess;

import java.util.Map;

public class Game {
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

    public Integer getScoreOf(String id) {
        if (result == GameResult.draw) {
            return 1;
        } else if (result == GameResult.unknown) {
            return null;
        } else {
            if (getWhiteId().equals(id)) {
                if (result == GameResult.white_won || result == GameResult.black_forfeit) {
                    return 2;
                } else if (result == GameResult.black_won || result == GameResult.white_forfeit) {
                    return 0;
                }
            } else if (getBlackId().equals(id)) {
                if (result == GameResult.black_won || result == GameResult.white_forfeit) {
                    return 2;
                } else if (result == GameResult.white_won || result == GameResult.black_forfeit) {
                    return 0;
                }
            }
        }
        throw new IllegalArgumentException(id);
    }

    public boolean isPlayedBy(String playerId) {
        return getWhiteId().equals(playerId) || getBlackId().equals(playerId);
    }

}
