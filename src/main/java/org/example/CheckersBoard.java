package org.example;

import net.CheckersClient;

import java.util.ArrayList;
import java.util.List;

public abstract class CheckersBoard{
    public static Square[][] board;
    public abstract int getSize();
    public abstract Square[][] getBoard();
    public abstract boolean getQueenLogic();
    public abstract boolean getBackwardsLogic();
    public abstract List<int[]> updateMove(int firstX, int firstY, int secondX, int secondY, boolean yourMove, boolean successiveCapMode);
    public int[] getJumpedPieceCoordinates(int firstClickX, int firstClickY, int secondClickX, int secondClickY) {
        final int xDelta = secondClickX-firstClickX;
        final int yDelta = secondClickY-firstClickY;
        if (Math.abs(xDelta-yDelta)!=1) {
            if (xDelta>0) {
                if (yDelta>0) {
                    return new int[]{secondClickX-1, secondClickY-1};
                } else {
                    return new int[]{secondClickX-1, secondClickY+1};
                }
            }
            else {
                if (yDelta>0) {
                    return new int[]{secondClickX+1, secondClickY-1};
                }
                else {
                    return new int[]{secondClickX+1, secondClickY+1};
                }
            }
        }
        return null;
    }
    public abstract void isSuccessiveCaptureAvailable(int[] attemptedMove);
    public List<int[]> checkForLegalMovesOnBoard() {
        List<int[]> notCaptureList = new ArrayList<>();
        List<int[]> captureList = new ArrayList<>();
        boolean captureAvailable = false;
        for (int i = 0; i<CheckersBoard.board.length; i++) {
            for (int j = 0; j < CheckersBoard.board.length; j++) {
                if ((i + j) % 2 != 0 && CheckersBoard.board[i][j].piece != null) {
                    Piece.PieceType type = CheckersBoard.board[i][j].piece.getPieceType();
                    boolean functionality;
                    if (type == Piece.PieceType.KING) {
                        functionality = this.getQueenLogic();
                    } else {
                        functionality = this.getBackwardsLogic();
                    }
                    if (CheckersBoard.board[i][j].piece.checkLegalMoves(functionality) != null && CheckersBoard.board[i][j].isTaken() && CheckersBoard.board[i][j].getTeam().equals(CheckersClient.frame.thisPlayerTeam)) {
                        for (int[] move : CheckersBoard.board[i][j].piece.checkLegalMoves(functionality)) {
                            if (move[2] > 0) {
                                captureList.add(new int[]{i, j, move[0], move[1], move[2], move[3]});
                                captureAvailable = true;
                                notCaptureList.clear();
                            } else if (!captureAvailable) {
                                notCaptureList.add(new int[]{i, j, move[0], move[1], move[2], move[3]});
                            }
                        }
                    }
                }
            }
        }
        if (captureAvailable) {
            return captureList;
        }
        else {
            return notCaptureList;
        }
    }
    public int[] checkForMoves(List<int[]> moveList, int[] coordinates) {
        for (int[] ints : moveList) {
            if (ints[0] == coordinates[0] && ints[1] == coordinates[1] && ints[2] == coordinates[2] && ints[3] == coordinates[3] ) {
                return ints;
            }
        }
        return null;
    }

}
