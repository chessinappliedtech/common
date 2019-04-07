package ru.appliedtech.chess.playoff.io;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import ru.appliedtech.chess.playoff.PlayoffSetup;

import java.io.IOException;

public class PlayoffSetupSerializer extends StdSerializer<PlayoffSetup> {
    public PlayoffSetupSerializer() {
        super(PlayoffSetup.class);
    }

    @Override
    public void serialize(PlayoffSetup value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("@type", value.getClass().getSimpleName());
        gen.writeStringField("game-result-system", value.getGameResultSystem().getName());
        gen.writeStringField("win-decision", value.getWinDecision().getName());
        gen.writeObjectField("level-setups", value.getLevelSetups());
        gen.writeEndObject();
    }
}
