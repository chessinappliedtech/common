package ru.appliedtech.chess.playoff.io;

import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import ru.appliedtech.chess.GameResultSystem;
import ru.appliedtech.chess.TournamentSetupObjectNodeReader;
import ru.appliedtech.chess.playoff.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PlayoffSetupObjectNodeReader implements TournamentSetupObjectNodeReader {
    @Override
    public PlayoffSetup read(ObjectNode node) {
        GameResultSystem gameResultSystem = null;
        TreeNode nodeGameResultSystem = node.get("game-result-system");
        if (nodeGameResultSystem instanceof TextNode) {
            String gameResultSystemName = ((TextNode) nodeGameResultSystem).asText();
            if ("STANDARD".equals(gameResultSystemName)) {
                gameResultSystem = GameResultSystem.STANDARD;
            }
        }
        WinDecision winDecision = null;
        TreeNode nodeWinDecision = node.get("win-decision");
        if (nodeWinDecision instanceof TextNode) {
            String winDecisionName = ((TextNode) nodeWinDecision).asText();
            if (ChunkToChunkWithArmageddonDecision.DECISION_NAME.equals(winDecisionName)) {
                winDecision = new ChunkToChunkWithArmageddonDecision();
            }
        }
        List<PlayoffLevelSetup> levelSetups = new ArrayList<>();
        TreeNode nodeLevelSetups = node.get("level-setups");
        if (nodeLevelSetups instanceof ArrayNode) {
            for (Iterator<JsonNode> it = ((ArrayNode) nodeLevelSetups).elements(); it.hasNext(); ) {
                ObjectNode levelSetupNode = (ObjectNode) it.next();
                PlayoffLevel level = PlayoffLevel.valueOf(levelSetupNode.get("level").asText());
                int classicRounds = levelSetupNode.get("classic-rounds").asInt();
                int rapidRounds = levelSetupNode.get("rapid-rounds").asInt();
                int blitzRounds = levelSetupNode.get("blitz-rounds").asInt();
                boolean armageddon = levelSetupNode.get("armageddon").asBoolean();
                levelSetups.add(new PlayoffLevelSetup(level, classicRounds, rapidRounds, blitzRounds, armageddon));
            }
        }
        return new PlayoffSetup(gameResultSystem, winDecision, levelSetups);
    }
}
