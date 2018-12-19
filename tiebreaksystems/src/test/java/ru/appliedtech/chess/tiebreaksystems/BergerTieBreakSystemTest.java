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
import static org.junit.Assert.assertTrue;
import static ru.appliedtech.chess.GameResultSystem.GameResultName.*;

public class BergerTieBreakSystemTest {

    @Test
    public void noGames() {
        List<Game> games = emptyList();
        BergerTieBreakSystem bergerTieBreakSystem = new BergerTieBreakSystem(games);
        BigDecimal score = bergerTieBreakSystem.scoreOf(createPlayer("player1"));
        assertEquals(new BigDecimal(0), score);
    }

    @Test
    public void oneGame() {
        GameResultSystem gameResultSystem = GameResultSystem.STANDARD;
        {
            Game game = createGame("player1", "player2", gameResultSystem.getResult(draw));
            BergerTieBreakSystem bergerTieBreakSystem = new BergerTieBreakSystem(singletonList(game));
            assertEquals(new BigDecimal(0.25), bergerTieBreakSystem.scoreOf(createPlayer("player1")));
            assertEquals(new BigDecimal(0.25), bergerTieBreakSystem.scoreOf(createPlayer("player2")));
            assertEquals(new BigDecimal(0), bergerTieBreakSystem.scoreOf(createPlayer("player3")));
        }
        {
            Game game = createGame("player1", "player2", gameResultSystem.getResult(white_won));
            BergerTieBreakSystem bergerTieBreakSystem = new BergerTieBreakSystem(singletonList(game));
            assertEquals(new BigDecimal(0), bergerTieBreakSystem.scoreOf(createPlayer("player1")));
            assertEquals(new BigDecimal(0), bergerTieBreakSystem.scoreOf(createPlayer("player2")));
            assertEquals(new BigDecimal(0), bergerTieBreakSystem.scoreOf(createPlayer("player3")));
        }
    }

    @Test
    public void twoGames() {
        GameResultSystem gameResultSystem = GameResultSystem.STANDARD;
        {
            Game game1 = createGame("player1", "player2", gameResultSystem.getResult(draw));
            Game game2 = createGame("player2", "player1", gameResultSystem.getResult(draw));
            BergerTieBreakSystem bergerTieBreakSystem = new BergerTieBreakSystem(asList(game1, game2));
            assertEquals(new BigDecimal(0.5), bergerTieBreakSystem.scoreOf(createPlayer("player1")).stripTrailingZeros());
            assertEquals(new BigDecimal(0.5), bergerTieBreakSystem.scoreOf(createPlayer("player2")).stripTrailingZeros());
            assertEquals(new BigDecimal(0), bergerTieBreakSystem.scoreOf(createPlayer("player3")).stripTrailingZeros());
        }
        {
            Game game1 = createGame("player1", "player2", gameResultSystem.getResult(white_won));
            Game game2 = createGame("player2", "player1", gameResultSystem.getResult(white_won));
            BergerTieBreakSystem bergerTieBreakSystem = new BergerTieBreakSystem(asList(game1, game2));
            assertEquals(new BigDecimal(0.5), bergerTieBreakSystem.scoreOf(createPlayer("player1")).stripTrailingZeros());
            assertEquals(new BigDecimal(0.5), bergerTieBreakSystem.scoreOf(createPlayer("player2")).stripTrailingZeros());
            assertEquals(new BigDecimal(0), bergerTieBreakSystem.scoreOf(createPlayer("player3")).stripTrailingZeros());
        }
        {
            Game game1 = createGame("player1", "player2", gameResultSystem.getResult(white_won));
            Game game2 = createGame("player2", "player1", gameResultSystem.getResult(black_won));
            BergerTieBreakSystem bergerTieBreakSystem = new BergerTieBreakSystem(asList(game1, game2));
            assertEquals(new BigDecimal(0), bergerTieBreakSystem.scoreOf(createPlayer("player1")).stripTrailingZeros());
            assertEquals(new BigDecimal(0), bergerTieBreakSystem.scoreOf(createPlayer("player2")).stripTrailingZeros());
        }
    }

    @Test
    public void wikipediaExample1() {
        GameResultSystem gameResultSystem = GameResultSystem.STANDARD;
        BergerTieBreakSystem bergerTieBreakSystem = new BergerTieBreakSystem(asList(
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
        ));
        assertEquals(0, new BigDecimal(11.75).compareTo(bergerTieBreakSystem.scoreOf(createPlayer("Иванов"))));
        assertEquals(0, new BigDecimal(10).compareTo(bergerTieBreakSystem.scoreOf(createPlayer("Петров"))));
        assertEquals(0, new BigDecimal(9).compareTo(bergerTieBreakSystem.scoreOf(createPlayer("Сидоров"))));
        assertEquals(0, new BigDecimal(7.75).compareTo(bergerTieBreakSystem.scoreOf(createPlayer("Кузнецов"))));
        assertEquals(0, new BigDecimal(3).compareTo(bergerTieBreakSystem.scoreOf(createPlayer("Смирнов"))));
        assertEquals(0, new BigDecimal(0).compareTo(bergerTieBreakSystem.scoreOf(createPlayer("Васильев"))));
        assertEquals(0, new BigDecimal(0).compareTo(bergerTieBreakSystem.scoreOf(createPlayer("Николаев"))));
    }

    @Test
    public void wikipediaExample2() {
        GameResultSystem gameResultSystem = GameResultSystem.STANDARD;
        BergerTieBreakSystem bergerTieBreakSystem = new BergerTieBreakSystem(asList(
                createGame("Sloth", "Zagorovsky", gameResultSystem.getResult(draw)),
                createGame("Sloth", "Kosenkov", gameResultSystem.getResult(draw)),
                createGame("Sloth", "Khasin", gameResultSystem.getResult(white_won)),
                createGame("Sloth", "Kletsel", gameResultSystem.getResult(draw)),
                createGame("Sloth", "De Carbonnel", gameResultSystem.getResult(draw)),
                createGame("Sloth", "Arnlind", gameResultSystem.getResult(white_won)),
                createGame("Sloth", "Dunhaupt", gameResultSystem.getResult(white_won)),
                createGame("Sloth", "Maedler", gameResultSystem.getResult(draw)),
                createGame("Sloth", "Estrin", gameResultSystem.getResult(white_won)),
                createGame("Sloth", "Walther", gameResultSystem.getResult(draw)),
                createGame("Boey", "Sloth", gameResultSystem.getResult(black_won)),
                createGame("Abramov", "Sloth", gameResultSystem.getResult(black_won)),
                createGame("Siklos", "Sloth", gameResultSystem.getResult(black_won)),
                createGame("Nun", "Sloth", gameResultSystem.getResult(black_won)),

                createGame("Zagorovsky", "Kosenkov", gameResultSystem.getResult(black_won)),
                createGame("Zagorovsky", "Khasin", gameResultSystem.getResult(draw)),
                createGame("Zagorovsky", "Kletsel", gameResultSystem.getResult(white_won)),
                createGame("Zagorovsky", "De Carbonnel", gameResultSystem.getResult(draw)),
                createGame("Zagorovsky", "Arnlind", gameResultSystem.getResult(white_won)),
                createGame("Zagorovsky", "Dunhaupt", gameResultSystem.getResult(white_won)),
                createGame("Zagorovsky", "Maedler", gameResultSystem.getResult(white_won)),
                createGame("Zagorovsky", "Estrin", gameResultSystem.getResult(draw)),
                createGame("Zagorovsky", "Walther", gameResultSystem.getResult(white_won)),
                createGame("Boey", "Zagorovsky", gameResultSystem.getResult(black_won)),
                createGame("Abramov", "Zagorovsky", gameResultSystem.getResult(black_won)),
                createGame("Siklos", "Zagorovsky", gameResultSystem.getResult(black_won)),
                createGame("Nun", "Zagorovsky", gameResultSystem.getResult(black_won)),

                createGame("Kosenkov", "Khasin", gameResultSystem.getResult(draw)),
                createGame("Kosenkov", "Kletsel", gameResultSystem.getResult(draw)),
                createGame("Kosenkov", "De Carbonnel", gameResultSystem.getResult(draw)),
                createGame("Kosenkov", "Arnlind", gameResultSystem.getResult(draw)),
                createGame("Kosenkov", "Dunhaupt", gameResultSystem.getResult(draw)),
                createGame("Kosenkov", "Maedler", gameResultSystem.getResult(white_won)),
                createGame("Kosenkov", "Estrin", gameResultSystem.getResult(white_won)),
                createGame("Kosenkov", "Walther", gameResultSystem.getResult(draw)),
                createGame("Boey", "Kosenkov", gameResultSystem.getResult(black_won)),
                createGame("Abramov", "Kosenkov", gameResultSystem.getResult(black_won)),
                createGame("Siklos", "Kosenkov", gameResultSystem.getResult(black_won)),
                createGame("Nun", "Kosenkov", gameResultSystem.getResult(black_won)),

                createGame("Khasin", "Kletsel", gameResultSystem.getResult(draw)),
                createGame("Khasin", "De Carbonnel", gameResultSystem.getResult(white_won)),
                createGame("Khasin", "Arnlind", gameResultSystem.getResult(draw)),
                createGame("Khasin", "Dunhaupt", gameResultSystem.getResult(black_won)),
                createGame("Khasin", "Maedler", gameResultSystem.getResult(white_won)),
                createGame("Khasin", "Estrin", gameResultSystem.getResult(white_won)),
                createGame("Khasin", "Walther", gameResultSystem.getResult(draw)),
                createGame("Boey", "Khasin", gameResultSystem.getResult(black_won)),
                createGame("Abramov", "Khasin", gameResultSystem.getResult(draw)),
                createGame("Siklos", "Khasin", gameResultSystem.getResult(black_won)),
                createGame("Nun", "Khasin", gameResultSystem.getResult(draw)),

                createGame("Kletsel", "De Carbonnel", gameResultSystem.getResult(draw)),
                createGame("Kletsel", "Arnlind", gameResultSystem.getResult(draw)),
                createGame("Kletsel", "Dunhaupt", gameResultSystem.getResult(draw)),
                createGame("Kletsel", "Maedler", gameResultSystem.getResult(draw)),
                createGame("Kletsel", "Estrin", gameResultSystem.getResult(black_won)),
                createGame("Kletsel", "Walther", gameResultSystem.getResult(white_won)),
                createGame("Boey", "Kletsel", gameResultSystem.getResult(black_won)),
                createGame("Abramov", "Kletsel", gameResultSystem.getResult(draw)),
                createGame("Siklos", "Kletsel", gameResultSystem.getResult(black_won)),
                createGame("Nun", "Kletsel", gameResultSystem.getResult(black_won)),

                createGame("De Carbonnel", "Arnlind", gameResultSystem.getResult(draw)),
                createGame("De Carbonnel", "Dunhaupt", gameResultSystem.getResult(draw)),
                createGame("De Carbonnel", "Maedler", gameResultSystem.getResult(black_won)),
                createGame("De Carbonnel", "Estrin", gameResultSystem.getResult(white_won)),
                createGame("De Carbonnel", "Walther", gameResultSystem.getResult(draw)),
                createGame("Boey", "De Carbonnel", gameResultSystem.getResult(draw)),
                createGame("Abramov", "De Carbonnel", gameResultSystem.getResult(white_won)),
                createGame("Siklos", "De Carbonnel", gameResultSystem.getResult(black_won)),
                createGame("Nun", "De Carbonnel", gameResultSystem.getResult(black_won)),

                createGame("Arnlind", "Dunhaupt", gameResultSystem.getResult(draw)),
                createGame("Arnlind", "Maedler", gameResultSystem.getResult(white_won)),
                createGame("Arnlind", "Estrin", gameResultSystem.getResult(black_won)),
                createGame("Arnlind", "Walther", gameResultSystem.getResult(draw)),
                createGame("Boey", "Arnlind", gameResultSystem.getResult(draw)),
                createGame("Abramov", "Arnlind", gameResultSystem.getResult(black_won)),
                createGame("Siklos", "Arnlind", gameResultSystem.getResult(black_won)),
                createGame("Nun", "Arnlind", gameResultSystem.getResult(draw)),

                createGame("Dunhaupt", "Maedler", gameResultSystem.getResult(black_won)),
                createGame("Dunhaupt", "Estrin", gameResultSystem.getResult(draw)),
                createGame("Dunhaupt", "Walther", gameResultSystem.getResult(white_won)),
                createGame("Boey", "Dunhaupt", gameResultSystem.getResult(white_won)),
                createGame("Abramov", "Dunhaupt", gameResultSystem.getResult(black_won)),
                createGame("Siklos", "Dunhaupt", gameResultSystem.getResult(draw)),
                createGame("Nun", "Dunhaupt", gameResultSystem.getResult(black_won)),

                createGame("Maedler", "Estrin", gameResultSystem.getResult(white_won)),
                createGame("Maedler", "Walther", gameResultSystem.getResult(draw)),
                createGame("Boey", "Maedler", gameResultSystem.getResult(draw)),
                createGame("Abramov", "Maedler", gameResultSystem.getResult(draw)),
                createGame("Siklos", "Maedler", gameResultSystem.getResult(draw)),
                createGame("Nun", "Maedler", gameResultSystem.getResult(black_won)),

                createGame("Estrin", "Walther", gameResultSystem.getResult(white_won)),
                createGame("Boey", "Estrin", gameResultSystem.getResult(black_won)),
                createGame("Abramov", "Estrin", gameResultSystem.getResult(black_won)),
                createGame("Siklos", "Estrin", gameResultSystem.getResult(white_won)),
                createGame("Nun", "Estrin", gameResultSystem.getResult(black_won)),

                createGame("Boey", "Walther", gameResultSystem.getResult(white_won)),
                createGame("Abramov", "Walther", gameResultSystem.getResult(black_won)),
                createGame("Siklos", "Walther", gameResultSystem.getResult(draw)),
                createGame("Nun", "Walther", gameResultSystem.getResult(black_won)),

                createGame("Abramov", "Boey", gameResultSystem.getResult(draw)),
                createGame("Siklos", "Boey", gameResultSystem.getResult(draw)),
                createGame("Nun", "Boey", gameResultSystem.getResult(black_won)),

                createGame("Siklos", "Abramov", gameResultSystem.getResult(draw)),
                createGame("Nun", "Abramov", gameResultSystem.getResult(black_won)),

                createGame("Nun", "Siklos", gameResultSystem.getResult(black_won))
        ));
        assertEquals(0, new BigDecimal(69.5).compareTo(bergerTieBreakSystem.scoreOf(createPlayer("Sloth"))));
        assertEquals(0, new BigDecimal(66.75).compareTo(bergerTieBreakSystem.scoreOf(createPlayer("Zagorovsky"))));
        assertEquals(0, new BigDecimal(67.5).compareTo(bergerTieBreakSystem.scoreOf(createPlayer("Kosenkov"))));
        assertEquals(0, new BigDecimal(54.75).compareTo(bergerTieBreakSystem.scoreOf(createPlayer("Khasin"))));
        assertEquals(0, new BigDecimal(47.75).compareTo(bergerTieBreakSystem.scoreOf(createPlayer("Kletsel"))));
        assertEquals(0, new BigDecimal(45.25).compareTo(bergerTieBreakSystem.scoreOf(createPlayer("De Carbonnel"))));
        assertEquals(0, new BigDecimal(42.5).compareTo(bergerTieBreakSystem.scoreOf(createPlayer("Arnlind"))));
        assertEquals(0, new BigDecimal(41.5).compareTo(bergerTieBreakSystem.scoreOf(createPlayer("Dunhaupt"))));
        assertEquals(0, new BigDecimal(41.5).compareTo(bergerTieBreakSystem.scoreOf(createPlayer("Maedler"))));
        assertEquals(0, new BigDecimal(40.5).compareTo(bergerTieBreakSystem.scoreOf(createPlayer("Estrin"))));
        assertEquals(0, new BigDecimal(33.25).compareTo(bergerTieBreakSystem.scoreOf(createPlayer("Walther"))));
        assertEquals(0, new BigDecimal(28.5).compareTo(bergerTieBreakSystem.scoreOf(createPlayer("Boey"))));
        assertEquals(0, new BigDecimal(24.75).compareTo(bergerTieBreakSystem.scoreOf(createPlayer("Abramov"))));
        assertEquals(0, new BigDecimal(22.75).compareTo(bergerTieBreakSystem.scoreOf(createPlayer("Siklos"))));
        assertEquals(0, new BigDecimal(7.75).compareTo(bergerTieBreakSystem.scoreOf(createPlayer("Nun"))));
    }

    private Game createGame(String whiteId, String blackId, GameResultSystem.GameResult gameResult) {
        return new Game(null, whiteId, blackId, null,
                gameResult, null, null);
    }

    private Player createPlayer(String playerId) {
        return new Player(playerId, null, null, null);
    }
}
