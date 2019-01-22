package ru.appliedtech.chess;

import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
import static ru.appliedtech.chess.GameResultSystem.GameResultName.white_won;

public class GameSerializationTest {
    private GameObjectMapper gameObjectMapper;
    private ObjectWriter objectWriter;

    public GameSerializationTest() {
        gameObjectMapper = new GameObjectMapper(new TestTournamentSetup());
        objectWriter = gameObjectMapper.writerWithDefaultPrettyPrinter();
    }

    @Test
    public void simple() throws IOException {
        Map<String, Object> outerServiceLinks = new HashMap<>();
        outerServiceLinks.put("lichess", "https://lichess.org/id1");
        HashMap<Object, Object> object = new HashMap<>();
        object.put("uri", "https://chess.com/id1");
        outerServiceLinks.put("chess.com", object);
        Game game = new Game("id1", "tournament", "white1", "black1", "01-12-2018",
                GameResultSystem.STANDARD.getResult(white_won), "data/id1_game.pgn", outerServiceLinks);
        String string = objectWriter.writeValueAsString(game);
        Game deserializedGame = gameObjectMapper.readValue(string, Game.class);
        assertEquals(game.getId(), deserializedGame.getId());
        assertEquals(game.getTournamentId(), deserializedGame.getTournamentId());
        assertEquals(game.getWhiteId(), deserializedGame.getWhiteId());
        assertEquals(game.getBlackId(), deserializedGame.getBlackId());
        assertEquals(game.getDate(), deserializedGame.getDate());
        assertEquals(game.getResult(), deserializedGame.getResult());
        assertEquals(game.getPgnLocation(), deserializedGame.getPgnLocation());
        assertEquals(game.getOuterServiceLinks(), deserializedGame.getOuterServiceLinks());
    }

    private static class TestTournamentSetup implements TournamentSetup {
        @Override
        public String getType() {
            return "test";
        }

        @Override
        public GameResultSystem getGameResultSystem() {
            return GameResultSystem.STANDARD;
        }

        @Override
        public List<String> getTieBreakSystems() {
            return emptyList();
        }

        @Override
        public TimeControlType getTimeControlType() {
            return TimeControlType.CLASSIC;
        }
    }
}
