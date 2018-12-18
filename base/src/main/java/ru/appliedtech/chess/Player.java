package ru.appliedtech.chess;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class Player {
    private final String id;
    private final String firstName;
    private final String lastName;
    private final Map<String, Object> outerServiceProfiles;

    public Player(String id,
                  String firstName,
                  String lastName,
                  Map<String, Object> outerServiceProfiles) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.outerServiceProfiles = outerServiceProfiles;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Map<String, Object> getOuterServiceProfiles() {
        return outerServiceProfiles;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Player.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("firstName='" + firstName + "'")
                .add("lastName='" + lastName + "'")
                .add("outerServiceProfiles=" + outerServiceProfiles)
                .toString();
    }
}
