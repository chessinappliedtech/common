package ru.appliedtech.chess.playoff;

import ru.appliedtech.chess.GameResultSystem;
import ru.appliedtech.chess.TournamentSetup;

import java.util.List;

public class PlayoffSetup implements TournamentSetup {
    private final GameResultSystem gameResultSystem;
    private final WinDecision winDecision;
    private final List<PlayoffLevelSetup> levelSetups;

    public PlayoffSetup(GameResultSystem gameResultSystem,
                        WinDecision winDecision,
                        List<PlayoffLevelSetup> levelSetups) {
        this.gameResultSystem = gameResultSystem;
        this.winDecision = winDecision;
        this.levelSetups = levelSetups;
    }

    @Override
    public String getType() {
        return "playoff";
    }

    @Override
    public GameResultSystem getGameResultSystem() {
        return gameResultSystem;
    }

    public List<PlayoffLevelSetup> getLevelSetups() {
        return levelSetups;
    }

    public WinDecision getWinDecision() {
        return winDecision;
    }
}
