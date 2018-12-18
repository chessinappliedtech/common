package ru.appliedtech.chess;

import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class GameSerializationTest {
    private ChessObjectMapper chessObjectMapper;
    private ObjectWriter chessObjectWriter;

    public GameSerializationTest() {
        chessObjectMapper = new ChessObjectMapper();
        chessObjectWriter = chessObjectMapper.writerWithDefaultPrettyPrinter();
    }

    @Test
    public void simple() throws IOException {
        Map<String, Object> outerServiceLinks = new HashMap<>();
        outerServiceLinks.put("lichess", "https://lichess.org/id1");
        HashMap<Object, Object> object = new HashMap<>();
        object.put("uri", "https://chess.com/id1");
        outerServiceLinks.put("chess.com", object);
        Game game = new Game("id1", "white1", "black1", "01-12-2018", GameResult.white_won, "data/id1_game.pgn", outerServiceLinks);
        String string = chessObjectWriter.writeValueAsString(game);
        Game deserializedGame = chessObjectMapper.readValue(string, Game.class);
        assertEquals(game.getId(), deserializedGame.getId());
        assertEquals(game.getWhiteId(), deserializedGame.getWhiteId());
        assertEquals(game.getBlackId(), deserializedGame.getBlackId());
        assertEquals(game.getDate(), deserializedGame.getDate());
        assertEquals(game.getResult(), deserializedGame.getResult());
        assertEquals(game.getPgnLocation(), deserializedGame.getPgnLocation());
        assertEquals(game.getOuterServiceLinks(), deserializedGame.getOuterServiceLinks());
    }
}
