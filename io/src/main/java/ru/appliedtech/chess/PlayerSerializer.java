package ru.appliedtech.chess;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class PlayerSerializer extends StdSerializer<Player> {
    protected PlayerSerializer() {
        super(Player.class);
    }

    @Override
    public void serialize(Player player, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("id", player.getId());
        jsonGenerator.writeStringField("firstName", player.getFirstName());
        jsonGenerator.writeStringField("lastName", player.getLastName());
        jsonGenerator.writeObjectField("profiles", player.getOuterServiceProfiles());
        jsonGenerator.writeEndObject();
    }
}
