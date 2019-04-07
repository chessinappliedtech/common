package ru.appliedtech.chess.playoff;

import ru.appliedtech.chess.*;
import ru.appliedtech.chess.storage.EloRatingReadOnlyStorage;
import ru.appliedtech.chess.storage.GameStorage;
import ru.appliedtech.chess.storage.KValueReadOnlyStorage;
import ru.appliedtech.chess.storage.PlayerStorage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class PlayoffLevelTable {
    private final PlayoffLevel level;
    private final PlayoffLevelPlayers levelPlayers;
    private final PlayerStorage playerStorage;
    private final GameStorage gameStorage;
    private final EloRatingReadOnlyStorage eloRatingStorage;
    private final KValueReadOnlyStorage kValueReadOnlyStorage;
    private final PlayoffSetup playoffSetup;
    private final TournamentDescription tournamentDescription;
    private Player player1;
    private Player player2;
    private Game armageddonGame;
    private List<Game> games;

    public PlayoffLevelTable(
            PlayoffLevel level,
            PlayoffLevelPlayers levelPlayers,
            PlayerStorage playerStorage,
            GameStorage gameStorage,
            EloRatingReadOnlyStorage eloRatingStorage,
            KValueReadOnlyStorage kValueReadOnlyStorage,
            PlayoffSetup playoffSetup,
            TournamentDescription tournamentDescription) {
        this.level = level;
        this.levelPlayers = levelPlayers;
        this.playerStorage = playerStorage;
        this.gameStorage = gameStorage;
        this.eloRatingStorage = eloRatingStorage;
        this.kValueReadOnlyStorage = kValueReadOnlyStorage;
        this.playoffSetup = playoffSetup;
        this.tournamentDescription = tournamentDescription;
        generate();
    }

    private void generate() {
        this.player1 = levelPlayers.getPlayerId1() != null
                ? playerStorage.getPlayer(levelPlayers.getPlayerId1()).orElseThrow(IllegalArgumentException::new)
                : null;
        this.player2 = levelPlayers.getPlayerId2() != null
                ? playerStorage.getPlayer(levelPlayers.getPlayerId2()).orElseThrow(IllegalArgumentException::new)
                : null;
        this.games = player1 != null && player2 != null
                ? gameStorage.getGames(
                        game -> game.isPlayedBy(player1.getId())
                                && game.isPlayedBy(player2.getId())
                                && game.getTournamentId().equals(tournamentDescription.getTournamentId()))
                : emptyList();
        this.armageddonGame = games.stream()
                .filter(game -> game.getTimeControlType() == TimeControlType.ARMAGEDDON)
                .findFirst()
                .orElse(null);
    }

    public PlayoffLevel getLevel() {
        return level;
    }

    public PlayoffLevelSetup getLevelSetup() {
        return playoffSetup.getLevelSetups().stream()
                .filter(setup -> setup.getLevel() == level)
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    public boolean arePlayersAssigned() {
        return player1 != null && player2 != null;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public List<BigDecimal> getGameScores(String playerId, TimeControlType timeControlType) {
        checkPlayer(playerId);
        List<BigDecimal> result = new ArrayList<>(getLevelSetup().getClassicRounds());
        List<BigDecimal> currentScores = games.stream()
                .filter(game -> game.getTimeControlType() == timeControlType)
                .map(game -> game.getScoreOf(playerId))
                .collect(toList());
        result.addAll(currentScores);
        for (int i = 0; i < getLevelSetup().getClassicRounds() - currentScores.size(); i++) {
            result.add(null);
        }
        return result;
    }

    private void checkPlayer(String playerId) {
        if (!player1.getId().equals(playerId) && !player2.getId().equals(playerId)) {
            throw new IllegalArgumentException(playerId);
        }
    }

    public BigDecimal getTotalScore(String playerId) {
        checkPlayer(playerId);
        List<BigDecimal> scores = new ArrayList<>(getGameScores(playerId, TimeControlType.CLASSIC));
        scores.addAll(getGameScores(playerId, TimeControlType.RAPID));
        scores.addAll(getGameScores(playerId, TimeControlType.BLITZ));
        getArmageddonScore(playerId).ifPresent(scores::add);
        return scores.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Optional<BigDecimal> getArmageddonScore(String playerId) {
        checkPlayer(playerId);
        return Optional.ofNullable(armageddonGame).map(g -> g.getScoreOf(playerId));
    }

    public MatchScore isWin(String playerId) {
        checkPlayer(playerId);
        int classicGamesPlayed = (int) games.stream().filter(game -> game.getTimeControlType() == TimeControlType.CLASSIC).count();
        BigDecimal classicGamesScore = games.stream().filter(game -> game.getTimeControlType() == TimeControlType.CLASSIC).map(game -> game.getScoreOf(playerId)).reduce(BigDecimal.ZERO, BigDecimal::add);
        int rapidGamesPlayed = (int) games.stream().filter(game -> game.getTimeControlType() == TimeControlType.RAPID).count();
        BigDecimal rapidGamesScore = games.stream().filter(game -> game.getTimeControlType() == TimeControlType.RAPID).map(game -> game.getScoreOf(playerId)).reduce(BigDecimal.ZERO, BigDecimal::add);
        int blitzGamesPlayed = (int) games.stream().filter(game -> game.getTimeControlType() == TimeControlType.BLITZ).count();
        BigDecimal blitzGamesScore = games.stream().filter(game -> game.getTimeControlType() == TimeControlType.BLITZ).map(game -> game.getScoreOf(playerId)).reduce(BigDecimal.ZERO, BigDecimal::add);
        return playoffSetup.getWinDecision().evaluateResult(
                getLevelSetup(),
                playoffSetup.getGameResultSystem(),
                classicGamesPlayed,
                classicGamesScore,
                rapidGamesPlayed,
                rapidGamesScore,
                blitzGamesPlayed,
                blitzGamesScore,
                armageddonGame != null,
                armageddonGame != null ? (armageddonGame.isWhite(playerId) ? Color.white : Color.black) : null,
                armageddonGame != null ? armageddonGame.getScoreOf(playerId) : null);
    }

    public Optional<Color> getArmageddonColor(String playerId) {
        checkPlayer(playerId);
        return Optional.ofNullable(armageddonGame).map(g -> g.isWhite(playerId) ? Color.white : Color.black);
    }
}
