package org.example;

import java.util.List;

public interface Piece {
    enum Team{BLACK, WHITE}
    List<int[]> checkLegalMoves();
    Team getTeam();
    void setWhite();
    void setBlack();
    void setCurrentSquare(Square square);
    //void setCurrentCoordinates(int x, int y);
    int[] getForwardIds();
    int[] getBackwardIds();
}
