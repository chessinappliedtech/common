package ru.appliedtech.chess.roundrobin.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;
import ru.appliedtech.chess.GameResultSystem;
import ru.appliedtech.chess.TimeControlType;
import ru.appliedtech.chess.roundrobin.RoundRobinSetup;

import java.util.HashMap;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class RoundRobinSetupSerializerTest {

    @Test
    public void serialize() {
        RoundRobinSetupSerializer serializer = new RoundRobinSetupSerializer();
        RoundRobinSetup setup = new RoundRobinSetup(
                4,
                GameResultSystem.STANDARD,
                asList("direct-encounter", "number-of-wins", "koya"),
                TimeControlType.CLASSIC);
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule("round-robin");
        module.addSerializer(serializer);
        mapper.registerModule(module);

        TypeFactory typeFactory = mapper.getTypeFactory();
        MapType mapType = typeFactory.constructMapType(HashMap.class, String.class, Object.class);
        HashMap<String, Object> serialized = mapper.convertValue(setup, mapType);
        assertEquals(4, serialized.get("rounds-amount"));
        assertEquals(GameResultSystem.STANDARD.getName(), serialized.get("game-result-system"));
        assertEquals(TimeControlType.CLASSIC.name(), serialized.get("time-control-type"));
        assertEquals(asList("direct-encounter", "number-of-wins", "koya"), serialized.get("tie-breaks"));
        assertEquals(RoundRobinSetup.class.getSimpleName(), serialized.get("@type"));
    }

}