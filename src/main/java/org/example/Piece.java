package org.example;

public interface Piece {
    void checkLegalMoves();
    Team getTeam();
    void setWhite();
    void setBlack();
    enum Team{BLACK, WHITE}
}
