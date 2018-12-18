package ru.appliedtech.chess.systems.roundRobin;

import ru.appliedtech.chess.TournamentSetup;

public class RoundRobinSetup implements TournamentSetup {
    private final int roundsAmount;

    public RoundRobinSetup(int roundsAmount) {
        this.roundsAmount = roundsAmount;
    }

    public int getRoundsAmount() {
        return roundsAmount;
    }

    @Override
    public String getType() {
        return "round-robin";
    }
}
