package ru.appliedtech.chess;

import java.util.List;

public interface TournamentSetup {
    String getType();
    GameResultSystem getGameResultSystem();
    List<String> getTieBreakSystems();
}
