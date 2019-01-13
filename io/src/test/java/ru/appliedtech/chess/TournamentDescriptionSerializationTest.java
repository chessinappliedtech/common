package ru.appliedtech.chess;

import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Test;

import java.io.IOException;

public class TournamentDescriptionSerializationTest {
    private ChessBaseObjectMapper chessBaseObjectMapper;
    private ObjectWriter chessObjectWriter;

    public TournamentDescriptionSerializationTest() {
//        chessBaseObjectMapper = new ChessBaseObjectMapper();
//        chessObjectWriter = chessBaseObjectMapper.writerWithDefaultPrettyPrinter();
    }

    @Test
    public void simple() throws IOException {
//        TournamentDescription tournamentDescription = new TournamentDescription(
//                "t1", "arb1", asList("dep1", "dep2"), asList("gw1", "gw2"),
//                "reg1", new RoundRobinSetup(2, GameResultSystem.STANDARD, asList("direct-encounter", "neustadtl")));
//        String resultString = chessObjectWriter.writeValueAsString(tournamentDescription);
//        TournamentDescription deserializedTournament = chessBaseObjectMapper.readValue(resultString, TournamentDescription.class);
//        assertEquals(tournamentDescription.getTournamentTitle(), deserializedTournament.getTournamentTitle());
//        assertEquals(tournamentDescription.getArbiter(), deserializedTournament.getArbiter());
//        assertEquals(tournamentDescription.getDeputyArbiters(), deserializedTournament.getDeputyArbiters());
//        assertEquals(tournamentDescription.getRegulations(), deserializedTournament.getRegulations());
//        assertEquals(tournamentDescription.getGameWriters(), deserializedTournament.getGameWriters());
//        assertEquals(RoundRobinSetup.class, deserializedTournament.getTournamentSetup().getClass());
//        assertEquals(2, ((RoundRobinSetup)deserializedTournament.getTournamentSetup()).getRoundsAmount());
//        assertEquals(asList("direct-encounter", "neustadtl"), deserializedTournament.getTournamentSetup().getTieBreakSystems());
    }
}
