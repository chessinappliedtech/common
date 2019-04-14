package ru.appliedtech.chess;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class GameObjectMapper extends ObjectMapper {
    public GameObjectMapper(TournamentSetup tournamentSetup) {
        this(tournamentSetup.getGameResultSystem());
    }

    public GameObjectMapper(GameResultSystem gameResultSystem) {
        super();
        SimpleModule module = new SimpleModule();
        module.addSerializer(new GameSerializer());
        module.addDeserializer(Game.class, new GameDeserializer(gameResultSystem));
        registerModule(module);
    }
}
