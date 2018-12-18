package ru.appliedtech.chess;

import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Test;
import ru.appliedtech.chess.systems.roundRobin.RoundRobinSetup;

import java.io.IOException;
import java.util.HashMap;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TournamentDescriptionSerializationTest {
    private ChessObjectMapper chessObjectMapper;
    private ObjectWriter chessObjectWriter;

    public TournamentDescriptionSerializationTest() {
        chessObjectMapper = new ChessObjectMapper();
        chessObjectWriter = chessObjectMapper.writerWithDefaultPrettyPrinter();
    }

    @Test
    public void simple() throws IOException {
        TournamentDescription tournamentDescription = new TournamentDescription(
                "t1", "arb1", asList("dep1", "dep2"), asList("gw1", "gw2"),
                "reg1", new RoundRobinSetup(2));
        String resultString = chessObjectWriter.writeValueAsString(tournamentDescription);
        TournamentDescription deserializedTournament = chessObjectMapper.readValue(resultString, TournamentDescription.class);
        assertEquals(tournamentDescription.getTournamentTitle(), deserializedTournament.getTournamentTitle());
        assertEquals(tournamentDescription.getArbiter(), deserializedTournament.getArbiter());
        assertEquals(tournamentDescription.getDeputyArbiters(), deserializedTournament.getDeputyArbiters());
        assertEquals(tournamentDescription.getRegulations(), deserializedTournament.getRegulations());
        assertEquals(tournamentDescription.getGameWriters(), deserializedTournament.getGameWriters());
        assertEquals(RoundRobinSetup.class, deserializedTournament.getTournamentSetup().getClass());
        assertEquals(2, ((RoundRobinSetup)deserializedTournament.getTournamentSetup()).getRoundsAmount());
    }
}
