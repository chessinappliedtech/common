package ru.appliedtech.chess;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.Map;

import static ru.appliedtech.chess.GameResultSystem.GameResult;
import static ru.appliedtech.chess.GameResultSystem.GameResultName;
import static ru.appliedtech.chess.GameResultSystem.GameResultName.resolve;
import static ru.appliedtech.chess.PlayerDeserializer.getString;
import static ru.appliedtech.chess.PlayerDeserializer.readMap;

public class GameDeserializer extends StdDeserializer<Game> {
    private final TournamentSetup tournamentSetup;

    GameDeserializer(TournamentSetup tournamentSetup) {
        super(Game.class);
        this.tournamentSetup = tournamentSetup;
    }

    @Override
    public Game deserialize(JsonParser jsonParser,
                            DeserializationContext deserializationContext) throws IOException {
        TreeNode treeNode = jsonParser.readValueAsTree();
        String id = getString(treeNode, "id");
        String tournamentId = getString(treeNode, "tournamentId");
        String whiteId = getString(treeNode, "whiteId");
        String blackId = getString(treeNode, "blackId");
        String date = getString(treeNode, "date");
        GameResultName resultName = resolve(getString(treeNode, "result"));
        GameResult gameResult = tournamentSetup.getGameResultSystem().getResult(resultName);
        String pgnLocation = getString(treeNode, "pgnLocation");
        Map<String, Object> outerServiceLinks = readMap(treeNode, "outerServiceLinks");
        TimeControlType timeControlType = TimeControlType.resolve(getString(treeNode, "timeControlType", "RAPID"));
        return new Game(id, tournamentId, whiteId, blackId, date, timeControlType, gameResult, pgnLocation, outerServiceLinks);
    }
}
