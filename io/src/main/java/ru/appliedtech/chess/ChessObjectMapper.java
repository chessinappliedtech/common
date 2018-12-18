package ru.appliedtech.chess;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import ru.appliedtech.chess.systems.roundRobin.RoundRobinSetupSerializer;

public class ChessObjectMapper extends ObjectMapper {
    public ChessObjectMapper() {
        super();
        SimpleModule module = new SimpleModule();
        module.addSerializer(new PlayerSerializer());
        module.addDeserializer(Player.class, new PlayerDeserializer());
        module.addSerializer(new GameSerializer());
        module.addDeserializer(Game.class, new GameDeserializer());
        module.addSerializer(new TournamentDescriptionSerializer());
        module.addDeserializer(TournamentDescription.class, new TournamentDescriptionDeserializer());
        module.addSerializer(new RoundRobinSetupSerializer());
        registerModule(module);
    }
}
