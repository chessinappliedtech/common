package ru.appliedtech.chess;

import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class PlayerSerializationTest {
    private ChessObjectMapper chessObjectMapper = new ChessObjectMapper();
    @Test
    public void simple() throws IOException {
        HashMap<String, Object> outerServiceProfiles = new HashMap<>();
        outerServiceProfiles.put("lichess", "MrJohnDoe");
        Player player = new Player("john.doe", "John", "Doe", outerServiceProfiles);
        String resultString = chessObjectMapper.writeValueAsString(player);
        Player deserializedPlayer = chessObjectMapper.readValue(resultString, Player.class);
        assertEquals(player.getId(), deserializedPlayer.getId());
        assertEquals(player.getFirstName(), deserializedPlayer.getFirstName());
        assertEquals(player.getLastName(), deserializedPlayer.getLastName());
        assertEquals(player.getOuterServiceProfiles(), deserializedPlayer.getOuterServiceProfiles());
    }
}
