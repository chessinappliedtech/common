package ru.appliedtech.chess;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class TournamentDescriptionSerializer extends StdSerializer<TournamentDescription> {
    protected TournamentDescriptionSerializer() {
        super(TournamentDescription.class);
    }

    @Override
    public void serialize(TournamentDescription tournamentDescription, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("tournamentTitle", tournamentDescription.getTournamentTitle());
        gen.writeStringField("tournamentId", tournamentDescription.getTournamentId());
        gen.writeStringField("arbiter", tournamentDescription.getArbiter());
        gen.writeStringField("regulations", tournamentDescription.getRegulations());
        gen.writeArrayFieldStart("deputyArbiters");
        for (String deputyArbiter : tournamentDescription.getDeputyArbiters()) {
            gen.writeString(deputyArbiter);
        }
        gen.writeEndArray();
        gen.writeArrayFieldStart("gameWriters");
        for (String gameWriter : tournamentDescription.getGameWriters()) {
            gen.writeString(gameWriter);
        }
        gen.writeEndArray();
        gen.writeObjectField("tournamentSetup", tournamentDescription.getTournamentSetup());
        gen.writeEndObject();
    }
}
