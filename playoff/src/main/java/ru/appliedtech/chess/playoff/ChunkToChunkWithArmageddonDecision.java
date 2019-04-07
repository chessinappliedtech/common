package ru.appliedtech.chess.playoff;

import ru.appliedtech.chess.Color;
import ru.appliedtech.chess.GameResultSystem;

import java.math.BigDecimal;

public class ChunkToChunkWithArmageddonDecision implements WinDecision {
    public static final String DECISION_NAME = "classic->rapid->blitz->armageddon";

    @Override
    public MatchScore evaluateResult(PlayoffLevelSetup playoffLevelSetup,
                                     GameResultSystem gameResultSystem,
                                     int classicGamesPlayed, BigDecimal classicScore,
                                     int rapidGamesPlayed, BigDecimal rapidScore,
                                     int blitzGamesPlayed, BigDecimal blitzScore,
                                     boolean armageddonPlayed, Color armageddonColor, BigDecimal armageddonScore) {
        if (playoffLevelSetup.getClassicRounds() != 0) {
            BigDecimal maxResult = gameResultSystem.getMaxResult(playoffLevelSetup.getClassicRounds());
            if (classicScore.multiply(new BigDecimal(2)).compareTo(maxResult) > 0) {
                return MatchScore.win;
            }
        }
        if (playoffLevelSetup.getClassicRounds() == classicGamesPlayed) {
            if (playoffLevelSetup.getRapidRounds() != 0) {
                BigDecimal maxResult = gameResultSystem.getMaxResult(playoffLevelSetup.getRapidRounds());
                if (rapidScore.multiply(new BigDecimal(2)).compareTo(maxResult) > 0) {
                    return MatchScore.win;
                } else if (playoffLevelSetup.getRapidRounds() == rapidGamesPlayed
                        && (rapidScore.multiply(new BigDecimal(2)).compareTo(maxResult) != 0)) {
                    return MatchScore.lose;
                }
            }
        }
        if (playoffLevelSetup.getClassicRounds() == classicGamesPlayed
                && playoffLevelSetup.getRapidRounds() == rapidGamesPlayed) {
            if (playoffLevelSetup.getBlitzRounds() != 0) {
                BigDecimal maxResult = gameResultSystem.getMaxResult(playoffLevelSetup.getBlitzRounds());
                if (blitzScore.multiply(new BigDecimal(2)).compareTo(maxResult) > 0) {
                    return MatchScore.win;
                } else if (playoffLevelSetup.getBlitzRounds() == blitzGamesPlayed
                        && (blitzScore.multiply(new BigDecimal(2)).compareTo(maxResult) != 0)) {
                    return MatchScore.lose;
                }
            }
        }
        if (playoffLevelSetup.hasArmageddon() && armageddonPlayed) {
            if (armageddonColor == Color.white) {
                if (armageddonScore.compareTo(BigDecimal.ONE) == 0) {
                    return MatchScore.win;
                }
                return MatchScore.lose;
            } else {
                if (armageddonScore.compareTo(BigDecimal.ZERO) == 0) {
                    return MatchScore.lose;
                }
                return MatchScore.win;
            }
        }
        return MatchScore.unknown;
    }

    @Override
    public String getName() {
        return DECISION_NAME;
    }
}
