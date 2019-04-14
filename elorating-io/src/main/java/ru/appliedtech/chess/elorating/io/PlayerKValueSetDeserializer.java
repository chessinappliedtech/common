package ru.appliedtech.chess.elorating.io;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import ru.appliedtech.chess.elorating.KValue;
import ru.appliedtech.chess.elorating.KValueSet;
import ru.appliedtech.chess.elorating.PlayerKValueSet;

import java.io.IOException;

import static ru.appliedtech.chess.PlayerDeserializer.getInt;
import static ru.appliedtech.chess.PlayerDeserializer.getString;

public class PlayerKValueSetDeserializer extends StdDeserializer<PlayerKValueSet> {
    public PlayerKValueSetDeserializer() {
        super(PlayerKValueSet.class);
    }

    @Override
    public PlayerKValueSet deserialize(JsonParser jsonParser,
                                       DeserializationContext deserializationContext)
            throws IOException {
        TreeNode treeNode = jsonParser.readValueAsTree();
        String playerId = getString(treeNode, "playerId");
        TreeNode kvaluesNode = treeNode.get("kvalues");
        KValueSet kvalueSet = null;
        if (kvaluesNode != null) {
            KValue classic = new KValue(getInt(treeNode, "classic", KValue.NEWBIES.value()));
            KValue rapid = new KValue(getInt(treeNode, "rapid", KValue.RAPID.value()));
            KValue blitz = new KValue(getInt(treeNode, "blitz", KValue.BLITZ.value()));
            kvalueSet = new KValueSet(classic, rapid, blitz);
        }
        return new PlayerKValueSet(playerId, kvalueSet);
    }
}
