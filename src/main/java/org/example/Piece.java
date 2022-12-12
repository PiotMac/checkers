package org.example;

public interface Piece {
    void checkLegalMoves();
    String getTeam();
    void setTeam(String team);
}
