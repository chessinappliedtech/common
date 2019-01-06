package ru.appliedtech.chess.storage;

import ru.appliedtech.chess.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

public class PlayerReadOnlyStorage implements PlayerStorage {

    private final List<Player> players;

    public PlayerReadOnlyStorage(List<Player> players) {
        this.players = players != null ? new ArrayList<>(players) : emptyList();
    }

    @Override
    public Optional<Player> getPlayer(String playerId) {
        return players.stream().filter(p -> p.getId().equals(playerId)).findFirst();
    }

    @Override
    public List<Player> getPlayers() {
        return unmodifiableList(players);
    }

    @Override
    public List<Player> getPlayers(Predicate<Player> predicate) {
        return players.stream().filter(predicate).collect(toList());
    }

}
