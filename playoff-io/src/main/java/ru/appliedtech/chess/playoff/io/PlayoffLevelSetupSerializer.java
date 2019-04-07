package ru.appliedtech.chess.playoff.io;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import ru.appliedtech.chess.playoff.PlayoffLevelSetup;

import java.io.IOException;

public class PlayoffLevelSetupSerializer  extends StdSerializer<PlayoffLevelSetup> {
    public PlayoffLevelSetupSerializer() {
        super(PlayoffLevelSetup.class);
    }

    @Override
    public void serialize(PlayoffLevelSetup value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeObjectField("level", value.getLevel());
        gen.writeNumberField("classic-rounds", value.getClassicRounds());
        gen.writeNumberField("rapid-rounds", value.getRapidRounds());
        gen.writeNumberField("blitz-rounds", value.getBlitzRounds());
        gen.writeBooleanField("armageddon", value.hasArmageddon());
        gen.writeEndObject();
    }
}
