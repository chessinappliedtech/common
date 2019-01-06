package ru.appliedtech.chess.storage;

import ru.appliedtech.chess.elorating.EloRating;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

public class EloRatingReadOnlyStorage {
    private final Map<EloRatingKey, EloRating> ratings;

    public EloRatingReadOnlyStorage(Map<EloRatingKey, EloRating> ratings) {
        this.ratings = ratings != null
                ? new HashMap<>(ratings)
                : new HashMap<>();
    }

    public Optional<EloRating> ratingOf(String playerId) {
        List<EloRating> history = ratingHistoryOf(playerId);
        return Optional.ofNullable(history.isEmpty() ? null : history.get(history.size() - 1));
    }

    public List<EloRating> ratingHistoryOf(String playerId) {
        return ratings.entrySet().stream()
                .filter(r -> r.getKey().getPlayerId().equals(playerId))
                .sorted(comparing(r -> r.getKey().getInstant()))
                .map(Map.Entry::getValue)
                .collect(toList());
    }

}
