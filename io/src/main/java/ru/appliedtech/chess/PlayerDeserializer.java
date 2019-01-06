package ru.appliedtech.chess;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.DecimalNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PlayerDeserializer extends StdDeserializer<Player> {
    protected PlayerDeserializer() {
        super(Player.class);
    }

    @Override
    public Player deserialize(JsonParser jsonParser,
                              DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        TreeNode treeNode = jsonParser.readValueAsTree();
        String id = getString(treeNode, "id");
        String firstName = getString(treeNode, "firstName");
        String lastName = getString(treeNode, "lastName");
        Map<String, Object> outerServiceProfiles = readMap(treeNode, "profiles");
        return new Player(id, firstName, lastName, outerServiceProfiles);
    }

    static Map<String, Object> readMap(TreeNode treeNode, String key) {
        TreeNode mapNode = treeNode.get(key);
        return readMap(mapNode);
    }

    private static Map<String, Object> readMap(TreeNode mapNode) {
        Map<String, Object> result = null;
        if (mapNode != null && mapNode.isObject() && mapNode instanceof ObjectNode) {
            result = new HashMap<>();
            for (Iterator<Map.Entry<String, JsonNode>> it = ((ObjectNode)mapNode).fields(); it.hasNext();) {
                Map.Entry<String, JsonNode> entry = it.next();
                JsonNode value = entry.getValue();
                if (value != null && value.isTextual()) {
                    result.put(entry.getKey(), value.asText());
                } else if (value != null && value.isObject()) {
                    result.put(entry.getKey(), readMap(value));
                } else {
                    result.put(entry.getKey(), value);
                }
            }
        }
        return result;
    }

    static String getString(TreeNode treeNode, String key) {
        String value = null;
        TreeNode node = treeNode.get(key);
        if (node != null && node.isValueNode() && node instanceof TextNode) {
            value = ((TextNode)node).asText();
        }
        return value;
    }

    private static BigDecimal getBigDecimal(TreeNode treeNode, String key) {
        BigDecimal value = null;
        TreeNode node = treeNode.get(key);
        if (node != null && node.isValueNode() && node instanceof DecimalNode) {
            value = ((DecimalNode)node).decimalValue();
        }
        return value;
    }

    private static int getInt(TreeNode treeNode, String key) {
        int value = 0;
        TreeNode node = treeNode.get(key);
        if (node != null && node.isValueNode() && node instanceof IntNode) {
            value = ((IntNode)node).intValue();
        }
        return value;
    }
}
