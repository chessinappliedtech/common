package ru.appliedtech.chess.storage;

import ru.appliedtech.chess.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

public class GameReadOnlyStorage implements GameStorage {
    private final List<Game> games;

    public GameReadOnlyStorage(List<Game> games) {
        this.games = games != null ? new ArrayList<>(games) : emptyList();
    }

    @Override
    public List<Game> getGames(String playerId1, String playerId2) {
        return getGames(game -> game.isPlayedBy(playerId1) && game.isPlayedBy(playerId2));
    }

    @Override
    public List<Game> getGames(Predicate<Game> predicate) {
        return games.stream().filter(predicate).collect(toList());
    }

    @Override
    public List<Game> getGames() {
        return unmodifiableList(games);
    }
}
