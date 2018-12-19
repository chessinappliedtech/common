package ru.appliedtech.chess.systems.swiss;

import ru.appliedtech.chess.GameResultSystem;
import ru.appliedtech.chess.TournamentSetup;

import java.util.List;

public class SwissSetup implements TournamentSetup {
    @Override
    public String getType() {
        return "swiss";
    }

    @Override
    public GameResultSystem getGameResultSystem() {
        return null;
    }

    @Override
    public List<String> getTieBreakSystems() {
        throw new UnsupportedOperationException();
    }
}
