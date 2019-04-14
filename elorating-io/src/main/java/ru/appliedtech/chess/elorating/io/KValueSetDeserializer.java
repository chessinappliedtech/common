package ru.appliedtech.chess.elorating.io;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import ru.appliedtech.chess.elorating.KValue;
import ru.appliedtech.chess.elorating.KValueSet;

import java.io.IOException;

import static ru.appliedtech.chess.PlayerDeserializer.getInt;

public class KValueSetDeserializer extends StdDeserializer<KValueSet> {
    protected KValueSetDeserializer() {
        super(KValueSet.class);
    }

    @Override
    public KValueSet deserialize(JsonParser jsonParser,
                                       DeserializationContext deserializationContext)
            throws IOException {
        TreeNode treeNode = jsonParser.readValueAsTree();
        KValue classic = new KValue(getInt(treeNode, "classic", KValue.NEWBIES.value()));
        KValue rapid = new KValue(getInt(treeNode, "rapid", KValue.RAPID.value()));
        KValue blitz = new KValue(getInt(treeNode, "blitz", KValue.BLITZ.value()));
        return new KValueSet(classic, rapid, blitz);
    }
}
