package ru.appliedtech.chess.roundrobin.io;

import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import ru.appliedtech.chess.GameResultSystem;
import ru.appliedtech.chess.TimeControlType;
import ru.appliedtech.chess.TournamentSetupObjectNodeReader;
import ru.appliedtech.chess.roundrobin.RoundRobinSetup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RoundRobinSetupObjectNodeReader implements TournamentSetupObjectNodeReader {
    @Override
    public RoundRobinSetup read(ObjectNode node) {
        TreeNode nodeRoundsAmount = node.get("rounds-amount");
        int roundsAmount = 1;
        if (nodeRoundsAmount instanceof IntNode) {
            roundsAmount = ((IntNode) nodeRoundsAmount).asInt();
        }
        List<String> tieBreakSystems = new ArrayList<>();
        TreeNode nodeTieBreaks = node.get("tie-breaks");
        if (nodeTieBreaks instanceof ArrayNode) {
            for (Iterator<JsonNode> it = ((ArrayNode) nodeTieBreaks).elements(); it.hasNext(); ) {
                String tieBreakSystemName = it.next().asText();
                tieBreakSystems.add(tieBreakSystemName);
            }
        }
        GameResultSystem gameResultSystem = null;
        TreeNode nodeGameResultSystem = node.get("game-result-system");
        if (nodeGameResultSystem instanceof TextNode) {
            String gameResultSystemName = ((TextNode) nodeGameResultSystem).asText();
            if ("STANDARD".equals(gameResultSystemName)) {
                gameResultSystem = GameResultSystem.STANDARD;
            }
        }
        TimeControlType timeControlType = null;
        TreeNode nodeTimeControlType = node.get("time-control-type");
        if (nodeTimeControlType instanceof TextNode) {
            String timeControlTypeName = ((TextNode) nodeTimeControlType).asText();
            timeControlType = TimeControlType.valueOf(timeControlTypeName);
        }
        return new RoundRobinSetup(roundsAmount, gameResultSystem, tieBreakSystems, timeControlType);
    }
}
