package ru.appliedtech.chess.storage;

import ru.appliedtech.chess.Player;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface PlayerStorage {
    Optional<Player> getPlayer(String playerId);
    List<Player> getPlayers();
    List<Player> getPlayers(Predicate<Player> predicate);
}
