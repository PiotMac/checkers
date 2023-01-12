package org.example;

import java.util.List;

/**
 * Interfejs pionków i królów
 */
public interface Piece {
    enum Team{BLACK, WHITE}
    enum PieceType{MAN, KING}
    List<int[]> checkLegalMoves(boolean functionality);
    Team getTeam();
    void setWhite();
    void setBlack();
    void setCurrentSquare(Square square);
    int[] getForwardIds();
    PieceType getPieceType();
}
