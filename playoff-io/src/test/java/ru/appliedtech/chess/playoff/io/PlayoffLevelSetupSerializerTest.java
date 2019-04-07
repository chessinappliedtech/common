package ru.appliedtech.chess.playoff.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;
import ru.appliedtech.chess.playoff.PlayoffLevel;
import ru.appliedtech.chess.playoff.PlayoffLevelSetup;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class PlayoffLevelSetupSerializerTest {

    @Test
    public void serialize() {
        PlayoffLevelSetupSerializer serializer = new PlayoffLevelSetupSerializer();
        PlayoffLevelSetup playoffLevelSetup = new PlayoffLevelSetup(PlayoffLevel.QUARTERFINAL,
                0, 2, 4, true);
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule("playoff");
        module.addSerializer(serializer);
        mapper.registerModule(module);

        TypeFactory typeFactory = mapper.getTypeFactory();
        MapType mapType = typeFactory.constructMapType(HashMap.class, String.class, Object.class);
        HashMap<String, Object> serialized = mapper.convertValue(playoffLevelSetup, mapType);
        assertEquals("QUARTERFINAL", serialized.get("level"));
        assertEquals(0, serialized.get("classic-rounds"));
        assertEquals(2, serialized.get("rapid-rounds"));
        assertEquals(4, serialized.get("blitz-rounds"));
        assertEquals(true, serialized.get("armageddon"));
    }
}
