package ru.appliedtech.chess.systems.roundRobin;

import ru.appliedtech.chess.GameResultSystem;
import ru.appliedtech.chess.TournamentSetup;

public class RoundRobinSetup implements TournamentSetup {
    private final int roundsAmount;
    private final GameResultSystem gameResultSystem;

    public RoundRobinSetup(int roundsAmount, GameResultSystem gameResultSystem) {
        this.roundsAmount = roundsAmount;
        this.gameResultSystem = gameResultSystem;
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
}
