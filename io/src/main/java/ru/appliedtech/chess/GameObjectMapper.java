package ru.appliedtech.chess;

import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import ru.appliedtech.chess.systems.roundRobin.RoundRobinSetupSerializer;

import java.util.HashMap;
import java.util.Map;

public class GameObjectMapper extends ObjectMapper {
    public GameObjectMapper(TournamentSetup tournamentSetup) {
        super();
        SimpleModule module = new SimpleModule();
        module.addSerializer(new GameSerializer());
        module.addDeserializer(Game.class, new GameDeserializer(tournamentSetup));
        registerModule(module);
    }
}
