package ru.appliedtech.chess.roundrobin.player_status;

import ru.appliedtech.chess.Game;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

public class Opponent {
    private final String id;
    private final List<Game> games;

    public Opponent(String id, List<Game> games) {
        this.id = id;
        this.games = games != null ? new ArrayList<>(games) : emptyList();
    }

    public List<Game> getGames() {
        return unmodifiableList(games);
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return id;
    }
}
