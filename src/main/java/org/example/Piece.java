package org.example;

import java.util.List;

public interface Piece {
    List<int[]> checkLegalMoves();
    Team getTeam();
    void setWhite();
    void setBlack();
    void setCurrentNeighbours(Square[] neighbours);
    void setCurrentCoordinates(int x, int y);
    int[] getForwardIds();
    int[] getBackwardIds();
    enum Team{BLACK, WHITE}
}
