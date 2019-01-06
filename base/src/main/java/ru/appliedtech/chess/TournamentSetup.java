package ru.appliedtech.chess;

import java.math.BigDecimal;
import java.util.List;

public interface TournamentSetup {
    String getType();
    GameResultSystem getGameResultSystem();
    List<String> getTieBreakSystems();
    TimeControlType getTimeControlType();

    default BigDecimal getDefaultInitialEloRating() {
        return new BigDecimal(1500);
    }
}
