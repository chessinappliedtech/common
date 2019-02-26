package ru.appliedtech.chess.roundrobin.io;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import ru.appliedtech.chess.roundrobin.RoundRobinSetup;

import java.io.IOException;

public class RoundRobinSetupSerializer extends StdSerializer<RoundRobinSetup> {
    public RoundRobinSetupSerializer() {
        super(RoundRobinSetup.class);
    }

    @Override
    public void serialize(RoundRobinSetup value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();

        gen.writeStringField("@type", value.getClass().getSimpleName());

        gen.writeNumberField("rounds-amount", value.getRoundsAmount());

        gen.writeArrayFieldStart("tie-breaks");
        for (String tieBreakSystem : value.getTieBreakSystems()) {
            gen.writeString(tieBreakSystem);
        }
        gen.writeEndArray();

        gen.writeStringField("game-result-system", value.getGameResultSystem().getName());

        gen.writeObjectField("time-control-type", value.getTimeControlType());

        gen.writeObjectFieldStart("color-allocating-system");
        gen.writeStringField("name", value.getColorAllocatingSystem());
        gen.writeNumberField("seed", value.getColorAllocatingSystemSeed());
        gen.writeEndObject();

        gen.writeEndObject();
    }
}
