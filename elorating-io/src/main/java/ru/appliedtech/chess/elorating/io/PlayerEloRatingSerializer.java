package ru.appliedtech.chess.elorating.io;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import ru.appliedtech.chess.elorating.PlayerEloRating;

import java.io.IOException;

public class PlayerEloRatingSerializer extends StdSerializer<PlayerEloRating> {
    public PlayerEloRatingSerializer() {
        super(PlayerEloRating.class);
    }

    @Override
    public void serialize(PlayerEloRating playerEloRating,
                          JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        if (playerEloRating != null) {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("playerId", playerEloRating.getPlayerId());
            if (playerEloRating.getClassicRating() != null && playerEloRating.getClassicRating().getValue() != null) {
                jsonGenerator.writeNumberField("classic", playerEloRating.getClassicRating().getValue());
            }
            if (playerEloRating.getRapidRating() != null && playerEloRating.getRapidRating().getValue() != null) {
                jsonGenerator.writeNumberField("rapid", playerEloRating.getRapidRating().getValue());
            }
            if (playerEloRating.getBlitzRating() != null && playerEloRating.getBlitzRating().getValue() != null) {
                jsonGenerator.writeNumberField("blitz", playerEloRating.getBlitzRating().getValue());
            }
            jsonGenerator.writeEndObject();
        }
    }
}
