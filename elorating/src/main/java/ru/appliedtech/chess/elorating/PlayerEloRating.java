package ru.appliedtech.chess.elorating;

import ru.appliedtech.chess.TimeControlType;

import java.text.MessageFormat;
import java.util.Optional;

public class PlayerEloRating {
    private final String playerId;
    private final EloRating classicRating;
    private final EloRating rapidRating;
    private final EloRating blitzRating;

    public PlayerEloRating(String playerId, EloRating classicRating, EloRating rapidRating, EloRating blitzRating) {
        this.playerId = playerId;
        this.classicRating = classicRating;
        this.rapidRating = rapidRating;
        this.blitzRating = blitzRating;
    }

    public String getPlayerId() {
        return playerId;
    }

    public EloRating getClassicRating() {
        return classicRating;
    }

    public EloRating getRapidRating() {
        return rapidRating;
    }

    public EloRating getBlitzRating() {
        return blitzRating;
    }

    public Optional<EloRating> getRating(TimeControlType timeControlType) {
        EloRating result;
        switch (timeControlType) {
            case CLASSIC:
                result = getClassicRating();
                break;
            case RAPID:
                result = getRapidRating();
                break;
            case BLITZ:
                result = getBlitzRating();
                break;
            default:
                result = null;
                break;
        }
        return Optional.ofNullable(result);
    }

    @Override
    public String toString() {
        return MessageFormat.format("{0} c->{1}, r->{2}, b->{3}",
                playerId, classicRating, rapidRating, blitzRating);
    }
}
