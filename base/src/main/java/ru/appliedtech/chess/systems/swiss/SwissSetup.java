package ru.appliedtech.chess.systems.swiss;

import ru.appliedtech.chess.TournamentSetup;

public class SwissSetup implements TournamentSetup {
    @Override
    public String getType() {
        return "swiss";
    }
}
