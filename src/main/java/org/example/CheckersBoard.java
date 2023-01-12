package org.example;

import java.util.List;

public abstract class CheckersBoard{
    public static Square[][] board;
    public abstract int getSize();
    public abstract Square[][] getBoard();
    public abstract boolean getQueenLogic();
    public abstract boolean getBackwardsLogic();
    public abstract List<int[]> updateMove(int firstX, int firstY, int secondX, int secondY, boolean yourMove, boolean successiveCapMode);
    public abstract int[] getJumpedPieceCoordinates(int firstClickX, int firstClickY, int secondClickX, int secondClickY);
    public abstract void isSuccessiveCaptureAvailable(int[] attemptedMove);
    public abstract List<int[]> checkForLegalMovesOnBoard();
    public abstract int[] checkForMoves(List<int[]> legalMovesBoard, int[] coordinates);
}
