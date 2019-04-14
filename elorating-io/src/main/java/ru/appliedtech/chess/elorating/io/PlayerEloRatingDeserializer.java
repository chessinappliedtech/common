package ru.appliedtech.chess.elorating.io;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import ru.appliedtech.chess.elorating.EloRating;
import ru.appliedtech.chess.elorating.PlayerEloRating;

import java.io.IOException;
import java.math.BigDecimal;

import static ru.appliedtech.chess.PlayerDeserializer.getBigDecimal;
import static ru.appliedtech.chess.PlayerDeserializer.getString;

public class PlayerEloRatingDeserializer extends StdDeserializer<PlayerEloRating> {
    public PlayerEloRatingDeserializer() {
        super(PlayerEloRating.class);
    }

    @Override
    public PlayerEloRating deserialize(JsonParser jsonParser,
                                       DeserializationContext deserializationContext)
            throws IOException {
        TreeNode treeNode = jsonParser.readValueAsTree();
        String playerId = getString(treeNode, "playerId");
        BigDecimal classicValue = getBigDecimal(treeNode, "classic");
        BigDecimal rapidValue = getBigDecimal(treeNode, "rapid");
        BigDecimal blitzValue = getBigDecimal(treeNode, "blitz");
        return new PlayerEloRating(
                playerId,
                classicValue != null ? new EloRating(classicValue) : null,
                rapidValue != null ? new EloRating(rapidValue) : null,
                blitzValue != null ? new EloRating(blitzValue) : null);
    }
}
