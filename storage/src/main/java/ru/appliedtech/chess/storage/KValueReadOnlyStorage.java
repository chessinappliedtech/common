package ru.appliedtech.chess.storage;

import ru.appliedtech.chess.elorating.KValueSet;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.emptyMap;

public class KValueReadOnlyStorage {
    private final Map<String, KValueSet> values;

    public KValueReadOnlyStorage(Map<String, KValueSet> values) {
        this.values = values != null ? new HashMap<>(values) : emptyMap();
    }

    public KValueSet getKValuesSet(String playerId) {
        return values.get(playerId);
    }

    public KValueSet getKValuesSet(String playerId, KValueSet defaultValue) {
        return values.getOrDefault(playerId, defaultValue);
    }
}
