package ru.appliedtech.chess;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.Map;

import static java.util.Arrays.stream;
import static ru.appliedtech.chess.PlayerDeserializer.getString;
import static ru.appliedtech.chess.PlayerDeserializer.readMap;

public class GameDeserializer extends StdDeserializer<Game> {
    protected GameDeserializer() {
        super(Game.class);
    }

    @Override
    public Game deserialize(JsonParser jsonParser,
                              DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        TreeNode treeNode = jsonParser.readValueAsTree();
        String id = getString(treeNode, "id");
        String whiteId = getString(treeNode, "whiteId");
        String blackId = getString(treeNode, "blackId");
        String date = getString(treeNode, "date");
        GameResult result = GameResult.resolve(getString(treeNode, "result"));
        String pgnLocation = getString(treeNode, "pgnLocation");
        Map<String, Object> outerServiceLinks = readMap(treeNode, "outerServiceLinks");
        return new Game(id, whiteId, blackId, date, result, pgnLocation, outerServiceLinks);
    }
}
