package org.example;

public interface Piece {
    void checkLegalMoves();
    Team getTeam();
    void setWhite();
    void setBlack();
    void setCurrentNeighbours(Square[] neighbours);
    void setCurrentCoordinates(int x, int y);
    int[] getForwardIds();
    int[] getBackwardIds();
    enum Team{BLACK, WHITE}
}
