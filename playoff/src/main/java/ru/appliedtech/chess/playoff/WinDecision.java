package ru.appliedtech.chess.playoff;

import ru.appliedtech.chess.Color;
import ru.appliedtech.chess.GameResultSystem;

import java.math.BigDecimal;

public interface WinDecision {
    MatchScore evaluateResult(
            PlayoffLevelSetup playoffLevelSetup,
            GameResultSystem gameResultSystem,
            int classicGamesPlayed, BigDecimal classicScore,
            int rapidGamesPlayed, BigDecimal rapidScore,
            int blitzGamesPlayed, BigDecimal blitzScore,
            boolean armageddonPlayed, Color armageddonColor, BigDecimal armageddonScore);

    String getName();
}
