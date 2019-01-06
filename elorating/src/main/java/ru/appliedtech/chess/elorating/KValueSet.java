package ru.appliedtech.chess.elorating;

import ru.appliedtech.chess.TimeControlType;

public class KValueSet {
    private final KValue classic;
    private final KValue rapid;
    private final KValue blitz;

    public KValueSet(KValue classic, KValue rapid, KValue blitz) {
        this.classic = classic;
        this.rapid = rapid;
        this.blitz = blitz;
    }

    public KValue forBlitz() {
        return blitz;
    }

    public KValue forClassic() {
        return classic;
    }

    public KValue forRapid() {
        return rapid;
    }

    public KValue get(TimeControlType timeControlType) {
        switch (timeControlType) {
            case BLITZ:
                return blitz;
            case RAPID:
                return rapid;
            case CLASSIC:
                return classic;
            case ARMAGEDDON:
            default:
                return KValue.ZERO;
        }
    }
}
