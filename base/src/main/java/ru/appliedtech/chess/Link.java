package ru.appliedtech.chess;

public class Link {
    private final String purpose;
    private final String value;
    private final String name;

    public Link(String purpose, String value, String name) {
        this.purpose = purpose;
        this.value = value;
        this.name = name;
    }

    public String getPurpose() {
        return purpose;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
