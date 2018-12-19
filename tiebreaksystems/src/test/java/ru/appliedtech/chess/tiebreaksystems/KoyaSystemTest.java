package ru.appliedtech.chess.tiebreaksystems;

import org.junit.Test;
import ru.appliedtech.chess.Game;
import ru.appliedtech.chess.GameResultSystem;
import ru.appliedtech.chess.Player;

import java.math.BigDecimal;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static ru.appliedtech.chess.GameResultSystem.GameResultName.draw;
import static ru.appliedtech.chess.GameResultSystem.GameResultName.white_won;

public class KoyaSystemTest {

    @Test
    public void noGames() {
        List<Game> games = emptyList();
        Player player = createPlayer("player1");
        KoyaSystem koyaSystem = new KoyaSystem(singletonList(player), games, new BigDecimal(2), 50);
        BigDecimal score = koyaSystem.scoreOf(player);
        assertEquals(new BigDecimal(0), score);
    }

    @Test
    public void wikipediaExample() {
        GameResultSystem gameResultSystem = GameResultSystem.STANDARD;
        KoyaSystem koyaSystem = new KoyaSystem(
            asList(
                createPlayer("Иванов"),
                createPlayer("Петров"),
                createPlayer("Сидоров"),
                createPlayer("Кузнецов"),
                createPlayer("Смирнов"),
                createPlayer("Васильев"),
                createPlayer("Николаев")),
            asList(
                createGame("Иванов", "Петров", gameResultSystem.getResult(draw)),
                createGame("Иванов", "Сидоров", gameResultSystem.getResult(draw)),
                createGame("Иванов", "Кузнецов", gameResultSystem.getResult(white_won)),
                createGame("Иванов", "Смирнов", gameResultSystem.getResult(white_won)),
                createGame("Иванов", "Васильев", gameResultSystem.getResult(white_won)),
                createGame("Иванов", "Николаев", gameResultSystem.getResult(white_won)),
                createGame("Петров", "Сидоров", gameResultSystem.getResult(draw)),
                createGame("Петров", "Кузнецов", gameResultSystem.getResult(draw)),
                createGame("Петров", "Смирнов", gameResultSystem.getResult(white_won)),
                createGame("Петров", "Васильев", gameResultSystem.getResult(white_won)),
                createGame("Петров", "Николаев", gameResultSystem.getResult(white_won)),
                createGame("Сидоров", "Кузнецов", gameResultSystem.getResult(draw)),
                createGame("Сидоров", "Смирнов", gameResultSystem.getResult(draw)),
                createGame("Сидоров", "Васильев", gameResultSystem.getResult(white_won)),
                createGame("Сидоров", "Николаев", gameResultSystem.getResult(white_won)),
                createGame("Кузнецов", "Смирнов", gameResultSystem.getResult(white_won)),
                createGame("Кузнецов", "Васильев", gameResultSystem.getResult(white_won)),
                createGame("Кузнецов", "Николаев", gameResultSystem.getResult(white_won)),
                createGame("Смирнов", "Васильев", gameResultSystem.getResult(white_won)),
                createGame("Смирнов", "Николаев", gameResultSystem.getResult(white_won)),
                createGame("Васильев", "Николаев", gameResultSystem.getResult(white_won))
            ),
            new BigDecimal(6),
            50);
        assertEquals(0, new BigDecimal(2).compareTo(koyaSystem.scoreOf(createPlayer("Иванов"))));
        assertEquals(0, new BigDecimal(1.5).compareTo(koyaSystem.scoreOf(createPlayer("Петров"))));
        assertEquals(0, new BigDecimal(1.5).compareTo(koyaSystem.scoreOf(createPlayer("Сидоров"))));
        assertEquals(0, new BigDecimal(1).compareTo(koyaSystem.scoreOf(createPlayer("Кузнецов"))));
        assertEquals(0, new BigDecimal(0.5).compareTo(koyaSystem.scoreOf(createPlayer("Смирнов"))));
        assertEquals(0, new BigDecimal(0).compareTo(koyaSystem.scoreOf(createPlayer("Васильев"))));
        assertEquals(0, new BigDecimal(0).compareTo(koyaSystem.scoreOf(createPlayer("Николаев"))));
    }

    private Game createGame(String whiteId, String blackId, GameResultSystem.GameResult gameResult) {
        return new Game(null, whiteId, blackId, null,
                gameResult, null, null);
    }

    private Player createPlayer(String playerId) {
        return new Player(playerId, null, null, null);
    }
}
