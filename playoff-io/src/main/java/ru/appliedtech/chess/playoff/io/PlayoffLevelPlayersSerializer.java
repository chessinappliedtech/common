package ru.appliedtech.chess.playoff.io;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import ru.appliedtech.chess.playoff.PlayoffLevelPlayers;

import java.io.IOException;

public class PlayoffLevelPlayersSerializer extends StdSerializer<PlayoffLevelPlayers> {
    public PlayoffLevelPlayersSerializer() {
        super(PlayoffLevelPlayers.class);
    }

    @Override
    public void serialize(PlayoffLevelPlayers value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeObjectField("level", value.getLevel());
        gen.writeStringField("player1", value.getPlayerId1());
        gen.writeStringField("player2", value.getPlayerId2());
        gen.writeEndObject();
    }
}
