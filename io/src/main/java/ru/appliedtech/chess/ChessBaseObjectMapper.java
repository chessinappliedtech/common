package ru.appliedtech.chess;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.util.Map;

public class ChessBaseObjectMapper extends ObjectMapper {
    public ChessBaseObjectMapper(Map<String, TournamentSetupObjectNodeReader> tournamentSetupObjectNodeReaders) {
        super();
        SimpleModule module = new SimpleModule("base");
        module.addSerializer(new PlayerSerializer());
        module.addDeserializer(Player.class, new PlayerDeserializer());
        module.addSerializer(new TournamentDescriptionSerializer());
        module.addDeserializer(TournamentDescription.class, new TournamentDescriptionDeserializer(tournamentSetupObjectNodeReaders));
        registerModule(module);
    }
}
