package ru.appliedtech.chess;

import java.math.BigDecimal;

public interface TournamentSetup {
    String getType();
    GameResultSystem getGameResultSystem();

    default BigDecimal getDefaultInitialEloRating() {
        return new BigDecimal(1500);
    }
}
