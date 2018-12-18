package ru.appliedtech.chess;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import ru.appliedtech.chess.systems.roundRobin.RoundRobinSetup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static ru.appliedtech.chess.PlayerDeserializer.getString;

public class TournamentDescriptionDeserializer extends StdDeserializer<TournamentDescription> {
    protected TournamentDescriptionDeserializer() {
        super(TournamentDescription.class);
    }

    @Override
    public TournamentDescription deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        TreeNode treeNode = parser.readValueAsTree();
        String tournamentTitle = getString(treeNode, "tournamentTitle");
        String arbiter = getString(treeNode, "arbiter");
        String regulations = getString(treeNode, "regulations");
        List<String> deputyArbiters = getStringArray(treeNode, "deputyArbiters");
        List<String> gameWriters = getStringArray(treeNode, "gameWriters");
        TreeNode nodeTournamentSetup = treeNode.get("tournamentSetup");
        TournamentSetup tournamentSetup = null;
        if (nodeTournamentSetup != null && nodeTournamentSetup.isObject() && nodeTournamentSetup instanceof ObjectNode) {
            TreeNode nodeType = nodeTournamentSetup.get("type");
            if (nodeType instanceof TextNode) {
                String systemType = ((TextNode) nodeType).asText();
                if ("round-robin".equals(systemType)) {
                    TreeNode nodeRoundsAmount = nodeTournamentSetup.get("roundsAmount");
                    int roundsAmount = 1;
                    if (nodeRoundsAmount instanceof IntNode) {
                        roundsAmount = ((IntNode) nodeRoundsAmount).asInt();
                    }
                    tournamentSetup = new RoundRobinSetup(roundsAmount);
                }
            }
        }
        return new TournamentDescription(tournamentTitle, arbiter, deputyArbiters, gameWriters, regulations, tournamentSetup);
    }

    private static List<String> getStringArray(TreeNode treeNode, String key) {
        List<String> result = new ArrayList<>();
        TreeNode dataNode = treeNode.get(key);
        if (dataNode != null && dataNode.isArray() && dataNode instanceof ArrayNode) {
            Iterator<JsonNode> elements = ((ArrayNode) dataNode).elements();
            elements.forEachRemaining(e -> {
                if (e instanceof TextNode) {
                    result.add(e.asText());
                }
            });
        }
        return result;
    }
}
