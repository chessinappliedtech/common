package ru.appliedtech.chess.playoff.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;
import ru.appliedtech.chess.GameResultSystem;
import ru.appliedtech.chess.playoff.ChunkToChunkWithArmageddonDecision;
import ru.appliedtech.chess.playoff.PlayoffLevel;
import ru.appliedtech.chess.playoff.PlayoffLevelSetup;
import ru.appliedtech.chess.playoff.PlayoffSetup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class PlayoffSetupSerializerTest {

    @Test
    public void serialize() {
        PlayoffSetupSerializer serializer = new PlayoffSetupSerializer();
        List<PlayoffLevelSetup> playoffLevels = new ArrayList<>();
        playoffLevels.add(new PlayoffLevelSetup(PlayoffLevel.SEMIFINAL, 0, 2, 0, true));
        playoffLevels.add(new PlayoffLevelSetup(PlayoffLevel.FOR_THIRD, 0, 2, 0, true));
        playoffLevels.add(new PlayoffLevelSetup(PlayoffLevel.FINAL, 0, 2, 2, true));
        PlayoffSetup playoffSetup = new PlayoffSetup(
                GameResultSystem.STANDARD,
                new ChunkToChunkWithArmageddonDecision(),
                playoffLevels);
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule("playoff");
        module.addSerializer(serializer);
        module.addSerializer(new PlayoffLevelSetupSerializer());
        mapper.registerModule(module);

        TypeFactory typeFactory = mapper.getTypeFactory();
        MapType mapType = typeFactory.constructMapType(HashMap.class, String.class, Object.class);
        HashMap<String, Object> serialized = mapper.convertValue(playoffSetup, mapType);
        assertEquals(PlayoffSetup.class.getSimpleName(), serialized.get("@type"));
        assertEquals("STANDARD", serialized.get("game-result-system"));
        assertEquals("classic->rapid->blitz->armageddon", serialized.get("win-decision"));
        List<Map<String, Object>> levelSetups = (List<Map<String, Object>>) serialized.get("level-setups");
        assertEquals(3, levelSetups.size());
        assertEquals("SEMIFINAL", levelSetups.get(0).get("level"));
        assertEquals(0, levelSetups.get(0).get("classic-rounds"));
        assertEquals(2, levelSetups.get(0).get("rapid-rounds"));
        assertEquals(0, levelSetups.get(0).get("blitz-rounds"));
        assertEquals(true, levelSetups.get(0).get("armageddon"));
        assertEquals("FOR_THIRD", levelSetups.get(1).get("level"));
        assertEquals(0, levelSetups.get(1).get("classic-rounds"));
        assertEquals(2, levelSetups.get(1).get("rapid-rounds"));
        assertEquals(0, levelSetups.get(1).get("blitz-rounds"));
        assertEquals(true, levelSetups.get(1).get("armageddon"));
        assertEquals("FINAL", levelSetups.get(2).get("level"));
        assertEquals(0, levelSetups.get(2).get("classic-rounds"));
        assertEquals(2, levelSetups.get(2).get("rapid-rounds"));
        assertEquals(2, levelSetups.get(2).get("blitz-rounds"));
        assertEquals(true, levelSetups.get(2).get("armageddon"));
    }
}
