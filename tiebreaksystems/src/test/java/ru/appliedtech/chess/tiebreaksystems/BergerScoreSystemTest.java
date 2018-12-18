package ru.appliedtech.chess.tiebreaksystems;

import org.junit.Test;
import ru.appliedtech.chess.Game;
import ru.appliedtech.chess.Player;

import java.math.BigDecimal;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;

public class BergerScoreSystemTest {

    @Test
    public void noGames() {
        List<Game> games = emptyList();
        BergerScoreSystem bergerScoreSystem = new BergerScoreSystem(games);
        BigDecimal score = bergerScoreSystem.scoreOf(createPlayer("player1"));
        assertEquals(new BigDecimal(0), score);
    }

    @Test
    public void oneGameDraw() {
//        Game game = createGame("game1", "player1", "player2", GameResultSystem.GameResult.draw);
//        BergerScoreSystem bergerScoreSystem = new BergerScoreSystem(singletonList(game));
//        assertEquals(new BigDecimal(0.25), bergerScoreSystem.scoreOf(createPlayer("player1")));
//        assertEquals(new BigDecimal(0.25), bergerScoreSystem.scoreOf(createPlayer("player2")));
//        assertEquals(new BigDecimal(0), bergerScoreSystem.scoreOf(createPlayer("player3")));
    }

//    private Game createGame(String gameId, String whiteId, String blackId, GameResult gameResult) {
//        return new Game(gameId, whiteId, blackId, null,
//                gameResult, null, null);
//    }

    private Player createPlayer(String playerId) {
        return new Player(playerId, null, null, null);
    }
}
