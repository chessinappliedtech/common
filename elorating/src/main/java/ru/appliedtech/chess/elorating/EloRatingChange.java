package ru.appliedtech.chess.elorating;

import java.math.BigDecimal;
import java.util.Objects;

public class EloRatingChange implements Comparable<EloRatingChange> {
    public static final EloRatingChange ZERO = new EloRatingChange(BigDecimal.ZERO);
    private final BigDecimal value;

    public EloRatingChange(BigDecimal value) {
        this.value = Objects.requireNonNull(value);
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    public int compareTo(EloRatingChange other) {
        return value.compareTo(other.value);
    }

    public EloRatingChange sum(EloRatingChange other) {
        return new EloRatingChange(getValue().add(other.getValue()));
    }
}
