package ru.appliedtech.chess.roundrobin;

import ru.appliedtech.chess.GameResultSystem;
import ru.appliedtech.chess.TimeControlType;
import ru.appliedtech.chess.TournamentSetup;
import ru.appliedtech.chess.elorating.KValue;
import ru.appliedtech.chess.elorating.KValueSet;

import java.util.ArrayList;
import java.util.List;

public class RoundRobinSetup implements TournamentSetup {
    private final int roundsAmount;
    private final GameResultSystem gameResultSystem;
    private final List<String> tieBreakSystems;
    private final TimeControlType timeControlType;

    public RoundRobinSetup(int roundsAmount,
                           GameResultSystem gameResultSystem,
                           List<String> tieBreakSystems,
                           TimeControlType timeControlType) {
        this.roundsAmount = roundsAmount;
        this.gameResultSystem = gameResultSystem;
        this.tieBreakSystems = new ArrayList<>(tieBreakSystems);
        this.timeControlType = timeControlType;
    }

    public int getRoundsAmount() {
        return roundsAmount;
    }

    @Override
    public String getType() {
        return "round-robin";
    }

    @Override
    public GameResultSystem getGameResultSystem() {
        return gameResultSystem;
    }

    @Override
    public List<String> getTieBreakSystems() {
        return tieBreakSystems;
    }

    @Override
    public TimeControlType getTimeControlType() {
        return timeControlType;
    }

    public KValueSet defaultKValueSet() {
        return new KValueSet(KValue.NEWBIES, KValue.RAPID, KValue.BLITZ);
    }

    public KValue selectKValue(KValueSet kValueSet) {
        kValueSet = kValueSet != null ? kValueSet : defaultKValueSet();
        return kValueSet.get(timeControlType);
    }
}
