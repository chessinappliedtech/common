package ru.appliedtech.chess;

import java.util.Date;
import java.util.List;

public final class TournamentDescription {
    private final String tournamentTitle;
    private final String tournamentId;
    private final String arbiter;
    private final List<String> players;
    private final List<String> deputyArbiters;
    private final List<String> gameWriters;
    private final String regulations;
    private final TournamentSetup tournamentSetup;
    private final Date startDay;
    private final List<String> joinedPlayers;
    private final List<String> quitPlayers;
    private final List<Link> links;

    public TournamentDescription(
            String tournamentTitle,
            String tournamentId, String arbiter,
            List<String> players, List<String> deputyArbiters,
            List<String> gameWriters,
            String regulations,
            Date startDay,
            TournamentSetup tournamentSetup,
            List<String> joinedPlayers,
            List<String> quitPlayers,
            List<Link> links) {
        this.tournamentTitle = tournamentTitle;
        this.tournamentId = tournamentId;
        this.arbiter = arbiter;
        this.players = players;
        this.deputyArbiters = deputyArbiters;
        this.gameWriters = gameWriters;
        this.regulations = regulations;
        this.startDay = startDay;
        this.tournamentSetup = tournamentSetup;
        this.joinedPlayers = joinedPlayers;
        this.quitPlayers = quitPlayers;
        this.links = links;
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

    public String getTournamentId() {
        return tournamentId;
    }

    public TournamentSetup getTournamentSetup() {
        return tournamentSetup;
    }

    public List<String> getPlayers() {
        return players;
    }

    public Date getStartDay() {
        return startDay;
    }

    public List<String> getJoinedPlayers() {
        return joinedPlayers;
    }

    public List<String> getQuitPlayers() {
        return quitPlayers;
    }

    public List<Link> getLinks() {
        return links;
    }
}
