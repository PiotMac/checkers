package org.example;

import java.util.List;

public interface Piece {
    enum Team{BLACK, WHITE}
    enum PieceType{MAN, KING}
    List<int[]> checkLegalMoves(boolean functionality);
    Team getTeam();
    void setWhite();
    void setBlack();
    void setCurrentSquare(Square square);
    //void setCurrentCoordinates(int x, int y);
    int[] getForwardIds();

    PieceType getPieceType();
}
