package ru.appliedtech.chess.systems.roundRobin;

import ru.appliedtech.chess.GameResultSystem;
import ru.appliedtech.chess.TournamentSetup;

import java.util.ArrayList;
import java.util.List;

public class RoundRobinSetup implements TournamentSetup {
    private final int roundsAmount;
    private final GameResultSystem gameResultSystem;
    private final List<String> tieBreakSystems;

    public RoundRobinSetup(int roundsAmount, GameResultSystem gameResultSystem, List<String> tieBreakSystems) {
        this.roundsAmount = roundsAmount;
        this.gameResultSystem = gameResultSystem;
        this.tieBreakSystems = new ArrayList<>(tieBreakSystems);
    }

    public int getRoundsAmount() {
        return roundsAmount;
    }

    @Override
    public String getType() {
        return "round-robin";
    }

    @Override
    public GameResultSystem getGameResultSystem() {
        return gameResultSystem;
    }

    @Override
    public List<String> getTieBreakSystems() {
        return tieBreakSystems;
    }
}
