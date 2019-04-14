package ru.appliedtech.chess.elorating.io;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import ru.appliedtech.chess.elorating.KValueSet;

import java.io.IOException;

public class KValueSetSerializer extends StdSerializer<KValueSet> {
    protected KValueSetSerializer() {
        super(KValueSet.class);
    }

    @Override
    public void serialize(KValueSet kValueSet,
                          JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        if (kValueSet != null) {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeNumberField("classic", kValueSet.forClassic().value());
            jsonGenerator.writeNumberField("rapid", kValueSet.forRapid().value());
            jsonGenerator.writeNumberField("blitz", kValueSet.forBlitz().value());
            jsonGenerator.writeEndObject();
        }
    }
}
