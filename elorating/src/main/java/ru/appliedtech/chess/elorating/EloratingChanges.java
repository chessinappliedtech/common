package ru.appliedtech.chess.elorating;

import ru.appliedtech.chess.Game;
import ru.appliedtech.chess.TimeControlType;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class EloratingChanges {
    private static final EloRating DEFAULT_RATING = new EloRating(
            new BigDecimal(1500.00).setScale(2, BigDecimal.ROUND_HALF_UP));

    public List<PlayerEloRatingChange> evaluateChanges(List<Game> games,
                                                        List<PlayerKValueSet> playerKValueSets,
                                                        List<PlayerEloRating> baselineRatings) {
        List<String> playerIds = games.stream()
                .flatMap(game -> Stream.of(game.getWhiteId(), game.getBlackId()))
                .distinct()
                .sorted()
                .collect(toList());
        return playerIds.stream()
                .map(playerId -> {
                    Map<TimeControlType, EloRatingChange> ratingChanges = games.stream()
                            .filter(game -> game.isPlayedBy(playerId))
                            .collect(groupingBy(Game::getTimeControlType)).entrySet().stream()
                            .collect(toMap(
                                    Map.Entry::getKey,
                                    entry -> {
                                        TimeControlType timeControlType = entry.getKey();
                                        List<Game> gamesPerTimeControlType = entry.getValue();
                                        return evaluateEloRatingChange(playerKValueSets, baselineRatings,
                                                playerId, timeControlType, gamesPerTimeControlType);
                                    }));
                    EloRatingChange classicRatingChange = ratingChanges.get(TimeControlType.CLASSIC);
                    EloRatingChange rapidRatingChange = ratingChanges.get(TimeControlType.RAPID);
                    EloRatingChange blitzRatingChange = ratingChanges.get(TimeControlType.BLITZ);
                    int classicGamesCount = (int) games.stream()
                            .filter(game -> game.isPlayedBy(playerId))
                            .filter(game -> game.getTimeControlType() == TimeControlType.CLASSIC)
                            .count();
                    int rapidGamesCount = (int) games.stream()
                            .filter(game -> game.isPlayedBy(playerId))
                            .filter(game -> game.getTimeControlType() == TimeControlType.RAPID)
                            .count();
                    int blitzGamesCount = (int) games.stream()
                            .filter(game -> game.isPlayedBy(playerId))
                            .filter(game -> game.getTimeControlType() == TimeControlType.BLITZ)
                            .count();
                    return new PlayerEloRatingChange(playerId,
                            classicRatingChange,
                            classicGamesCount,
                            rapidRatingChange,
                            rapidGamesCount,
                            blitzRatingChange,
                            blitzGamesCount);
                })
                .collect(toList());
    }

    public List<PlayerEloRating> evaluateNewRatings(List<PlayerEloRating> baselineRatings,
                                                      List<PlayerEloRatingChange> changes) {
        Map<String, PlayerEloRating> baselineRatingsMap = baselineRatings.stream().collect(toMap(PlayerEloRating::getPlayerId, r -> r));
        Map<String, PlayerEloRatingChange> changesMap = changes.stream().collect(toMap(PlayerEloRatingChange::getPlayerId, r -> r));
        List<PlayerEloRating> newRatings = new ArrayList<>();
        for (PlayerEloRatingChange change : changes) {
            String playerId = change.getPlayerId();
            PlayerEloRating baselineRating = baselineRatingsMap.get(playerId);
            PlayerEloRating newRating;
            if (baselineRating != null) {
                EloRatingChange classicRatingChange = change.getClassicRatingChange();
                EloRating newClassicRating;
                if (classicRatingChange != null) {
                    newClassicRating = baselineRating.getClassicRating() != null
                            ? baselineRating.getClassicRating().sum(classicRatingChange)
                            : DEFAULT_RATING.sum(classicRatingChange);
                } else {
                    newClassicRating = baselineRating.getClassicRating();
                }
                EloRatingChange rapidRatingChange = change.getRapidRatingChange();
                EloRating newRapidRating;
                if (rapidRatingChange != null) {
                    newRapidRating = baselineRating.getRapidRating() != null
                            ? baselineRating.getRapidRating().sum(rapidRatingChange)
                            : DEFAULT_RATING.sum(rapidRatingChange);
                } else {
                    newRapidRating = baselineRating.getRapidRating();
                }
                EloRatingChange blitzRatingChange = change.getBlitzRatingChange();
                EloRating newBlitzRating;
                if (blitzRatingChange != null) {
                    newBlitzRating = baselineRating.getBlitzRating() != null
                            ? baselineRating.getBlitzRating().sum(blitzRatingChange)
                            : DEFAULT_RATING.sum(blitzRatingChange);
                } else {
                    newBlitzRating = baselineRating.getBlitzRating();
                }
                newRating = new PlayerEloRating(playerId, newClassicRating, newRapidRating, newBlitzRating);
            } else {
                EloRatingChange classicRatingChange = change.getClassicRatingChange();
                EloRating newClassicRating = null;
                if (classicRatingChange != null) {
                    newClassicRating = DEFAULT_RATING.sum(classicRatingChange);
                }
                EloRatingChange rapidRatingChange = change.getRapidRatingChange();
                EloRating newRapidRating = null;
                if (rapidRatingChange != null) {
                    newRapidRating = DEFAULT_RATING.sum(rapidRatingChange);
                }
                EloRatingChange blitzRatingChange = change.getBlitzRatingChange();
                EloRating newBlitzRating = null;
                if (blitzRatingChange != null) {
                    newBlitzRating = DEFAULT_RATING.sum(blitzRatingChange);
                }
                newRating = new PlayerEloRating(playerId, newClassicRating, newRapidRating, newBlitzRating);
            }
            newRatings.add(newRating);
        }
        Set<String> unchangedPlayers = new HashSet<>(baselineRatingsMap.keySet());
        unchangedPlayers.removeAll(changesMap.keySet());
        newRatings.addAll(
                baselineRatingsMap.entrySet().stream()
                        .filter(e -> unchangedPlayers.contains(e.getKey()))
                        .map(Map.Entry::getValue)
                        .collect(toList()));
        return newRatings;
    }

    private EloRatingChange evaluateEloRatingChange(List<PlayerKValueSet> playerKValueSets,
                                                    List<PlayerEloRating> baselineRatings,
                                                    String playerId,
                                                    TimeControlType timeControlType,
                                                    List<Game> games) {
        return games.stream()
                .map(toRatingChange(playerKValueSets, baselineRatings, playerId, timeControlType))
                .reduce(EloRatingChange.ZERO, EloRatingChange::sum);
    }

    private Function<Game, EloRatingChange> toRatingChange(List<PlayerKValueSet> playerKValueSets,
                                                           List<PlayerEloRating> baselineRatings,
                                                           String playerId,
                                                           TimeControlType timeControlType) {
        EloRatingCalculator calculator = new EloRatingCalculator();
        EloRating baselineRating = ratingOf(playerId, baselineRatings)
                .flatMap(rating -> rating.getRating(timeControlType))
                .orElse(DEFAULT_RATING);
        KValue kValue = kValueOf(playerId, playerKValueSets).get(timeControlType);
        return game -> {
            EloRating opponentRating = ratingOf(game.getOpponentOf(playerId), baselineRatings)
                    .flatMap(rating -> rating.getRating(timeControlType))
                    .orElse(DEFAULT_RATING);
            return calculator.ratingChange(
                    baselineRating,
                    kValue,
                    opponentRating,
                    game.getScoreOf(playerId));
        };
    }

    private Optional<PlayerEloRating> ratingOf(String playerId, List<PlayerEloRating> ratings) {
        return ratings.stream()
                .filter(playerEloRating -> playerEloRating.getPlayerId().equals(playerId))
                .findFirst();
    }

    private KValueSet kValueOf(String playerId, List<PlayerKValueSet> kValueSets) {
        return kValueSets.stream()
                .filter(set -> set.getPlayerId().equals(playerId))
                .findFirst()
                .map(PlayerKValueSet::getKValueSet)
                .orElse(new KValueSet(KValue.NEWBIES, KValue.RAPID, KValue.BLITZ));
    }
}
