package ru.appliedtech.chess.tiebreaksystems;

import ru.appliedtech.chess.Game;
import ru.appliedtech.chess.Player;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.emptyList;

public final class DirectEncounterSystem implements ScoringTieBreakSystem {
    private final List<Game> games;

    public DirectEncounterSystem(List<Game> games) {
        this.games = games != null ? new ArrayList<>(games) : emptyList();
    }

    @Override
    public BigDecimal scoreOf(Player player) {
        String playerId = player.getId();
        return games.stream()
                .filter(game -> game.isPlayedBy(playerId))
                .map(game -> game.getScoreOf(playerId))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public String getName() {
        return "direct-encounter";
    }
}
