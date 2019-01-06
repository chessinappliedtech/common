package ru.appliedtech.chess.elorating;

import java.math.BigDecimal;
import java.util.Objects;

public class EloRating implements Comparable<EloRating> {
    private final BigDecimal value;

    public EloRating(BigDecimal value) {
        this.value = Objects.requireNonNull(value);
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    public int compareTo(EloRating other) {
        return value.compareTo(other.value);
    }

    public EloRating sum(EloRatingChange change) {
        return new EloRating(getValue().add(change.getValue()));
    }
}
