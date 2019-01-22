package ru.appliedtech.chess.storage;

import ru.appliedtech.chess.Game;

import java.util.List;
import java.util.function.Predicate;

public interface GameStorage {
    List<Game> getGames(String tournamentId, String playerId1, String playerId2);
    List<Game> getGames(String playerId1, String playerId2);
    List<Game> getGames(Predicate<Game> predicate);
    List<Game> getGames();
}
