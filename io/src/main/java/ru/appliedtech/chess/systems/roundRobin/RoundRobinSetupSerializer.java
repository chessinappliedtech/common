package ru.appliedtech.chess.systems.roundRobin;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class RoundRobinSetupSerializer extends StdSerializer<RoundRobinSetup> {
    public RoundRobinSetupSerializer() {
        super(RoundRobinSetup.class);
    }

    @Override
    public void serialize(RoundRobinSetup value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("type", value.getType());
        gen.writeNumberField("roundsAmount", value.getRoundsAmount());
        gen.writeArrayFieldStart("tie-breaks");
        for (String tieBreakSystem : value.getTieBreakSystems()) {
            gen.writeString(tieBreakSystem);
        }
        gen.writeEndArray();
        gen.writeEndObject();
    }
}
