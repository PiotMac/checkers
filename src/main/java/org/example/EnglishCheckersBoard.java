package org.example;

import net.CheckersClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EnglishCheckersBoard extends CheckersBoard {

    private static int lim;
    private static int rows;
    private static final boolean queenMovesOnDiagonals = true;
    private static final boolean movingBackwards = false;

    public EnglishCheckersBoard() {
        lim = 8;
        rows = 3;
        board = new Square[lim][lim];
    }
    @Override
    public int getSize() {
        return lim;
    }

    @Override
    public Square[][] getBoard() {
        createBoard();
        return board;
    }
    @Override
    public boolean getQueenLogic() {
        return queenMovesOnDiagonals;
    }
    @Override
    public boolean getBackwardsLogic() {
        return movingBackwards;
    }

    @Override
    public List<int[]> updateMove(int firstX, int firstY, int secondX, int secondY, boolean yourMove, boolean successiveCapMode) {
        List<int[]> squaresToUpdate = new ArrayList<>();
        board[firstX][firstY].setTaken(false);
        squaresToUpdate.add(new int[] {firstX, firstY});
        squaresToUpdate.add(new int[] {secondX, secondY});
        boolean addedKing = false;

        int[] potentiallyJumped = getJumpedPieceCoordinates(firstX, firstY, secondX, secondY);
        if (potentiallyJumped!=null) {
            board[potentiallyJumped[0]][potentiallyJumped[1]].setTaken(false);
            squaresToUpdate.add(potentiallyJumped);
        }

        if (secondX==lim-1) {
            if (CheckersClient.frame.thisPlayerTeam == Piece.Team.WHITE && yourMove) {
                board[secondX][secondY].setPiece(CheckersClient.frame.thisPlayerTeam, Piece.PieceType.KING);
            } else if (CheckersClient.frame.thisPlayerTeam == Piece.Team.BLACK && !yourMove) {
                board[secondX][secondY].setPiece(CheckersClient.frame.anotherPlayerTeam, Piece.PieceType.KING);
            }
            clearSuccessive();
            successiveCapMode=false;
            addedKing = true;
        }
        if (secondX==0) {
            if (CheckersClient.frame.thisPlayerTeam == Piece.Team.BLACK && yourMove) {
                board[secondX][secondY].setPiece(CheckersClient.frame.thisPlayerTeam, Piece.PieceType.KING);
            }
            else if (CheckersClient.frame.thisPlayerTeam == Piece.Team.WHITE && !yourMove) {
                board[secondX][secondY].setPiece(CheckersClient.frame.anotherPlayerTeam, Piece.PieceType.KING);
            }
            clearSuccessive();
            successiveCapMode=false;
            addedKing = true;
        }
        if(!successiveCapMode) {
            if (yourMove) {
                if (!addedKing) {
                    board[secondX][secondY].setPiece(CheckersClient.frame.thisPlayerTeam, CheckersClient.frame.attemptedMovedPieceType);
                }
                CheckersClient.frame.setTitle("Opponent's turn!");
                //super.setTitle("Opponent's turn!");
                System.out.println("Successful move!");
            }
            else {
                if (!addedKing) {
                    board[secondX][secondY].setPiece(CheckersClient.frame.anotherPlayerTeam, CheckersClient.frame.attemptedMovedPieceType);
                }
                CheckersClient.frame.setTitle("Your turn!");
                //super.setTitle("Your turn!");
                System.out.println("Opponent made a move");
            }

        }
        else {
            if (yourMove) {
                board[secondX][secondY].setPiece(CheckersClient.frame.thisPlayerTeam, CheckersClient.frame.attemptedMovedPieceType);
                System.out.println("Successive capture possible!");
            }
            else {
                board[secondX][secondY].setPiece(CheckersClient.frame.anotherPlayerTeam, CheckersClient.frame.attemptedMovedPieceType);
                System.out.println("You fell victim to a successive capture");
            }

        }
        return squaresToUpdate;
    }

    @Override
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

    @Override
    public void isSuccessiveCaptureAvailable(int[] attemptedMove) {
        Square[][] boardClone = CheckersBoard.board.clone();
        boolean goodMove = false;
        boardClone[attemptedMove[0]][attemptedMove[1]].setTaken(false);
        boardClone[attemptedMove[2]][attemptedMove[3]].setPiece(CheckersClient.frame.thisPlayerTeam, CheckersClient.frame.attemptedMovedPieceType);
        CheckersClient.frame.successiveJumpedXYs.add(getJumpedPieceCoordinates(attemptedMove[0],attemptedMove[1],attemptedMove[2],attemptedMove[3]));
        List<int[]> possibleNextMoves = boardClone[attemptedMove[2]][attemptedMove[3]].piece.checkLegalMoves();
        if (possibleNextMoves!=null) {
            for (int[] move : possibleNextMoves) {
                if (move[2]>0) {
                    goodMove=true;
                    for (int[] jumpedXY : CheckersClient.frame.successiveJumpedXYs) {
                        if (Arrays.equals(getJumpedPieceCoordinates(attemptedMove[2], attemptedMove[3], move[0], move[1]),jumpedXY)) {
                            goodMove=false;
                            break;
                        }
                    }
                    if (goodMove) {
                        CheckersClient.frame.successiveCaptureMode = true;
                        CheckersClient.frame.successiveX=attemptedMove[2];
                        CheckersClient.frame.successiveY=attemptedMove[3];
                        break;
                    }
                }
            }
        }
        if (!goodMove) {
            CheckersClient.frame.reprintBoard(CheckersClient.frame.successiveJumpedXYs);
            clearSuccessive();
        }
    }

    @Override
    public List<int[]> checkForLegalMovesOnBoard() {
        List<int[]> notCaptureList = new ArrayList<>();
        List<int[]> captureList = new ArrayList<>();
        boolean captureAvailable = false;
        for (int i = 0; i<CheckersBoard.board.length; i++) {
            for (int j = 0; j<CheckersBoard.board.length; j++) {
                if ((i+j)%2!=0 && CheckersBoard.board[i][j].piece!=null && CheckersBoard.board[i][j].piece.checkLegalMoves()!=null && CheckersBoard.board[i][j].isTaken() && CheckersBoard.board[i][j].getTeam().equals(CheckersClient.frame.thisPlayerTeam)) {
                    for (int[] move : CheckersBoard.board[i][j].piece.checkLegalMoves()) {
                        if (move[2]>0) {
                            captureList.add(new int[]{i,j,move[0],move[1],move[2],move[3]});
                            captureAvailable = true;
                            notCaptureList.clear();
                        }
                        else if (!captureAvailable) {
                            notCaptureList.add(new int[]{i,j,move[0],move[1],move[2],move[3]});
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

    @Override
    public int[] checkForMoves(List<int[]> moveList, int[] coordinates) {
        for (int[] ints : moveList) {
            if (ints[0] == coordinates[0] && ints[1] == coordinates[1] && ints[2] == coordinates[2] && ints[3] == coordinates[3] ) {
                return ints;
            }
        }
        return null;
    }

    private void clearSuccessive() {
        CheckersClient.frame.successiveCaptureMode = false;
        CheckersClient.frame.successiveX=-1;
        CheckersClient.frame.successiveY=-1;
        CheckersClient.frame.successiveJumpedXYs.clear();
    }

    private void createBoard() {
        for (int i = 0; i < lim; i++) {
            for (int j = 0; j < lim; j++) {
                if ((i+j)%2 != 0) {
                    board[i][j] = new Square(lim);
                    board[i][j].setCoordinates(i, j);
                }
                else {
                    board[i][j] = null;
                }
            }
        }
        //For every playable square set neighbours
        for (int i = 0; i < lim; i++) {
            for (int j = 0; j < lim; j++) {
                if ((i+j)%2 != 0) {
                    board[i][j].setNeighbours();
                }
            }
        }
        //Set starting pieces
        for (int i = 0; i < lim; i++) {
            for (int j = 0; j < lim; j++) {
                if ((i+j)%2 != 0 && i < rows) {
                    board[i][j].setPiece(Piece.Team.WHITE, Piece.PieceType.MAN);
                }
                else if ((i+j)%2 != 0 && i > lim-rows-1) {
                    board[i][j].setPiece(Piece.Team.BLACK, Piece.PieceType.MAN);
                }
            }
        }
    }
}
