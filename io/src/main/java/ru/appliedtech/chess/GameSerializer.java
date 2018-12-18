package ru.appliedtech.chess;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class GameSerializer extends StdSerializer<Game> {
    protected GameSerializer() {
        super(Game.class);
    }

    @Override
    public void serialize(Game game, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("id", game.getId());
        jsonGenerator.writeStringField("whiteId", game.getWhiteId());
        jsonGenerator.writeStringField("blackId", game.getBlackId());
        jsonGenerator.writeStringField("date", game.getDate());
        jsonGenerator.writeStringField("result", game.getResult().name());
        jsonGenerator.writeStringField("pgnLocation", game.getPgnLocation());
        jsonGenerator.writeObjectField("outerServiceLinks", game.getOuterServiceLinks());
        jsonGenerator.writeEndObject();
    }
}
