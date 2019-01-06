package ru.appliedtech.chess.elorating;

public class KValue {
    public static final KValue ZERO = new KValue(0);
    public static final KValue STRONG = new KValue(10);
    public static final KValue STANDARD = new KValue(20);
    public static final KValue NEWBIES = new KValue(40);
    public static final KValue RAPID = new KValue(20);
    public static final KValue BLITZ = new KValue(20);

    private final int kvalue;

    public KValue(int kvalue) {
        this.kvalue = kvalue;
    }

    public int value() {
        return kvalue;
    }

    @Override
    public String toString() {
        return String.valueOf(value());
    }
}
