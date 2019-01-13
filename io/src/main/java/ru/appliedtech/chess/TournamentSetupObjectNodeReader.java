package ru.appliedtech.chess;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface TournamentSetupObjectNodeReader {
    TournamentSetup read(ObjectNode node);
}
