package ru.appliedtech.chess;

import java.util.List;

public class TournamentDescription {
    private final String tournamentTitle;
    private final String arbiter;
    private final List<String> deputyArbiters;
    private final List<String> gameWriters;
    private final String regulations;
    private final TournamentSetup tournamentSetup;

    public TournamentDescription(
            String tournamentTitle,
            String arbiter,
            List<String> deputyArbiters,
            List<String> gameWriters,
            String regulations,
            TournamentSetup tournamentSetup) {
        this.tournamentTitle = tournamentTitle;
        this.arbiter = arbiter;
        this.deputyArbiters = deputyArbiters;
        this.gameWriters = gameWriters;
        this.regulations = regulations;
        this.tournamentSetup = tournamentSetup;
    }

    public String getRegulations() {
        return regulations;
    }

    public List<String> getDeputyArbiters() {
        return deputyArbiters;
    }

    public List<String> getGameWriters() {
        return gameWriters;
    }

    public String getArbiter() {
        return arbiter;
    }

    public String getTournamentTitle() {
        return tournamentTitle;
    }

    public TournamentSetup getTournamentSetup() {
        return tournamentSetup;
    }
}
