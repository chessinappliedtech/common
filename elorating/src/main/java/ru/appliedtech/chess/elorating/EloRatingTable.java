package ru.appliedtech.chess.elorating;

import ru.appliedtech.chess.Player;

import java.util.List;
import java.util.Optional;

public class EloRatingTable {
    private final List<Player> players;
    private final List<PlayerEloRatingChange> playerEloRatingChanges;
    private final List<PlayerEloRating> newRatings;

    public EloRatingTable(List<Player> players,
                          List<PlayerEloRatingChange> playerEloRatingChanges,
                          List<PlayerEloRating> newRatings) {
        this.players = players;
        this.playerEloRatingChanges = playerEloRatingChanges;
        this.newRatings = newRatings;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Optional<Integer> getRapidGamesCount(String playerId) {
        return playerEloRatingChanges.stream()
                .filter(change -> change.getPlayerId().equals(playerId))
                .findFirst()
                .map(PlayerEloRatingChange::getRapidGamesCount);
    }

    public Optional<Integer> getBlitzGamesCount(String playerId) {
        return playerEloRatingChanges.stream()
                .filter(change -> change.getPlayerId().equals(playerId))
                .findFirst()
                .map(PlayerEloRatingChange::getBlitzGamesCount);
    }

    public Optional<Integer> getClassicGamesCount(String playerId) {
        return playerEloRatingChanges.stream()
                .filter(change -> change.getPlayerId().equals(playerId))
                .findFirst()
                .map(PlayerEloRatingChange::getClassicGamesCount);
    }

    public Optional<EloRatingChange> getRapidRatingChange(String playerId) {
        return playerEloRatingChanges.stream()
                .filter(change -> change.getPlayerId().equals(playerId))
                .findFirst()
                .map(PlayerEloRatingChange::getRapidRatingChange);
    }

    public Optional<EloRatingChange> getBlitzRatingChange(String playerId) {
        return playerEloRatingChanges.stream()
                .filter(change -> change.getPlayerId().equals(playerId))
                .findFirst()
                .map(PlayerEloRatingChange::getBlitzRatingChange);
    }

    public Optional<EloRatingChange> getClassicRatingChange(String playerId) {
        return playerEloRatingChanges.stream()
                .filter(change -> change.getPlayerId().equals(playerId))
                .findFirst()
                .map(PlayerEloRatingChange::getClassicRatingChange);
    }

    public Optional<EloRating> getRapidRating(String playerId) {
        return newRatings.stream()
                .filter(rating -> rating.getPlayerId().equals(playerId))
                .findFirst()
                .map(PlayerEloRating::getRapidRating);
    }

    public Optional<EloRating> getBlitzRating(String playerId) {
        return newRatings.stream()
                .filter(rating -> rating.getPlayerId().equals(playerId))
                .findFirst()
                .map(PlayerEloRating::getBlitzRating);
    }

    public Optional<EloRating> getClassicRating(String playerId) {
        return newRatings.stream()
                .filter(rating -> rating.getPlayerId().equals(playerId))
                .findFirst()
                .map(PlayerEloRating::getClassicRating);
    }
}
