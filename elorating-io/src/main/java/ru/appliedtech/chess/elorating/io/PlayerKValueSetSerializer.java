package ru.appliedtech.chess.elorating.io;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import ru.appliedtech.chess.elorating.PlayerKValueSet;

import java.io.IOException;

public class PlayerKValueSetSerializer extends StdSerializer<PlayerKValueSet> {
    public PlayerKValueSetSerializer() {
        super(PlayerKValueSet.class);
    }

    @Override
    public void serialize(PlayerKValueSet playerKValueSet,
                          JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        if (playerKValueSet != null) {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("playerId", playerKValueSet.getPlayerId());
            if (playerKValueSet.getKValueSet() != null) {
                jsonGenerator.writeObjectField("kvalues", playerKValueSet.getKValueSet());
            }
            jsonGenerator.writeEndObject();
        }
    }
}
