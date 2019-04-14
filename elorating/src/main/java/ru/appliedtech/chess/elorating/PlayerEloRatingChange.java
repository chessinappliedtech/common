package ru.appliedtech.chess.elorating;

import ru.appliedtech.chess.TimeControlType;

import java.text.MessageFormat;
import java.util.Optional;

public class PlayerEloRatingChange {
    private final String playerId;
    private final EloRatingChange classicRatingChange;
    private final int classicGamesCount;
    private final EloRatingChange rapidRatingChange;
    private final int rapidGamesCount;
    private final EloRatingChange blitzRatingChange;
    private final int blitzGamesCount;

    public PlayerEloRatingChange(String playerId,
                                 EloRatingChange classicRatingChange,
                                 int classicGamesCount,
                                 EloRatingChange rapidRatingChange,
                                 int rapidGamesCount,
                                 EloRatingChange blitzRatingChange,
                                 int blitzGamesCount) {
        this.playerId = playerId;
        this.classicRatingChange = classicRatingChange;
        this.classicGamesCount = classicGamesCount;
        this.rapidRatingChange = rapidRatingChange;
        this.rapidGamesCount = rapidGamesCount;
        this.blitzRatingChange = blitzRatingChange;
        this.blitzGamesCount = blitzGamesCount;
    }

    public String getPlayerId() {
        return playerId;
    }

    public EloRatingChange getClassicRatingChange() {
        return classicRatingChange;
    }

    public EloRatingChange getRapidRatingChange() {
        return rapidRatingChange;
    }

    public EloRatingChange getBlitzRatingChange() {
        return blitzRatingChange;
    }

    public int getBlitzGamesCount() {
        return blitzGamesCount;
    }

    public int getClassicGamesCount() {
        return classicGamesCount;
    }

    public int getRapidGamesCount() {
        return rapidGamesCount;
    }

    public int getGamesCount(TimeControlType timeControlType) {
        switch (timeControlType) {
            case CLASSIC:
                return getClassicGamesCount();
            case RAPID:
                return getRapidGamesCount();
            case BLITZ:
                return getBlitzGamesCount();
            default:
                return 0;
        }
    }

    public Optional<EloRatingChange> getRatingChange(TimeControlType timeControlType) {
        EloRatingChange result;
        switch (timeControlType) {
            case CLASSIC:
                result = getClassicRatingChange();
                break;
            case RAPID:
                result = getRapidRatingChange();
                break;
            case BLITZ:
                result = getBlitzRatingChange();
                break;
            default:
                result = null;
                break;
        }
        return Optional.ofNullable(result);
    }

    @Override
    public String toString() {
        return MessageFormat.format("{0} c[{1}]->{2}, r[{3}]->{4}, b[{5}]->{6}",
                playerId,
                classicGamesCount, classicRatingChange,
                rapidGamesCount, rapidRatingChange,
                blitzGamesCount, blitzRatingChange);
    }
}
