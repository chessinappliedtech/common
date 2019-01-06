package ru.appliedtech.chess.roundrobin;

import ru.appliedtech.chess.Game;
import ru.appliedtech.chess.Player;
import ru.appliedtech.chess.tiebreaksystems.*;

import java.math.BigDecimal;
import java.util.List;

public class RoundRobinTieBreakSystemFactory {
    private final List<Player> players;
    private final List<Game> games;
    private final RoundRobinSetup roundRobinSetup;

    public RoundRobinTieBreakSystemFactory(List<Player> players, List<Game> games, RoundRobinSetup roundRobinSetup) {
        this.players = players;
        this.games = games;
        this.roundRobinSetup = roundRobinSetup;
    }

    public TieBreakSystem create(String name) {
        switch (name) {
            case "direct-encounter":
                return new DirectEncounterSystem(games);
            case "number-of-wins":
                return new GreaterNumberOfWinsSystem(games);
            case "neustadtl":
                return new NeustadtlSystem(games);
            case "koya":
                return new KoyaSystem(players, games, new BigDecimal(roundRobinSetup.getRoundsAmount()), 50);
            default:
                throw new IllegalArgumentException(name);
        }
    }
}
