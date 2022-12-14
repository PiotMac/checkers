package org.example;

public interface Piece {
    void checkLegalMoves();
    Team getTeam();
    void setWhite();
    void setBlack();
    void setCurrentNeighbours(Square[] neighbours);
    void setCurrentCoordinates(int x, int y);
    enum Team{BLACK, WHITE}
}
