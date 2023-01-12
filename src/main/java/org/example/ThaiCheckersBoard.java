package org.example;

import net.CheckersClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ThaiCheckersBoard extends CheckersBoard{
    private static int lim;
    private static int rows;
    private static final boolean queenMovesOnDiagonals = true;
    private static final boolean movingBackwards = false;

    public ThaiCheckersBoard() {
        lim = 8;
        rows = 2;
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
        if (yourMove && ((secondX==lim-1 && CheckersClient.frame.thisPlayerTeam == Piece.Team.WHITE) || (secondX==0 && CheckersClient.frame.thisPlayerTeam == Piece.Team.BLACK))){
            board[secondX][secondY].setPiece(CheckersClient.frame.thisPlayerTeam, Piece.PieceType.KING);
            addedKing = true;
        } else if (!yourMove && ((secondX==lim-1 && CheckersClient.frame.thisPlayerTeam == Piece.Team.BLACK) || (secondX==0 && CheckersClient.frame.thisPlayerTeam == Piece.Team.WHITE))) {
            board[secondX][secondY].setPiece(CheckersClient.frame.anotherPlayerTeam, Piece.PieceType.KING);
            addedKing = true;
        }
        if (!addedKing && yourMove) {
            board[secondX][secondY].setPiece(CheckersClient.frame.thisPlayerTeam, CheckersClient.frame.attemptedMovedPieceType);
        } else if (!addedKing) {
            board[secondX][secondY].setPiece(CheckersClient.frame.anotherPlayerTeam, CheckersClient.frame.attemptedMovedPieceType);
        }
        if(!successiveCapMode) {
            if (yourMove) {
                CheckersClient.frame.setTitle("Opponent's turn!");
                System.out.println("Successful move!");
            }
            else {
                CheckersClient.frame.setTitle("Your turn!");
                System.out.println("Opponent made a move");
            }

        }
        else {
            if (yourMove) {
                System.out.println("Successive capture possible!");
            }
            else {
                System.out.println("You fell victim to a successive capture");
            }
        }
        return squaresToUpdate;
    }
    @Override
    public void isSuccessiveCaptureAvailable(int[] attemptedMove) {
        Square[][] boardClone = CheckersBoard.board.clone();
        boolean goodMove = false;
        Piece.PieceType checkedPieceType;
        boardClone[attemptedMove[0]][attemptedMove[1]].setTaken(false);
        if (attemptedMove[2]==0 && CheckersClient.frame.thisPlayerTeam== Piece.Team.BLACK) {
            boardClone[attemptedMove[2]][attemptedMove[3]].setPiece(CheckersClient.frame.thisPlayerTeam, Piece.PieceType.KING);
            checkedPieceType = Piece.PieceType.KING;
        } else if (attemptedMove[2]==lim-1 && CheckersClient.frame.thisPlayerTeam== Piece.Team.WHITE) {
            boardClone[attemptedMove[2]][attemptedMove[3]].setPiece(CheckersClient.frame.thisPlayerTeam, Piece.PieceType.KING);
            checkedPieceType = Piece.PieceType.KING;
        } else {
            boardClone[attemptedMove[2]][attemptedMove[3]].setPiece(CheckersClient.frame.thisPlayerTeam, CheckersClient.frame.attemptedMovedPieceType);
            checkedPieceType = CheckersClient.frame.attemptedMovedPieceType;
        }
        int[] takenPieceCoordinates = getJumpedPieceCoordinates(attemptedMove[0], attemptedMove[1], attemptedMove[2], attemptedMove[3]);
        boardClone[takenPieceCoordinates[0]][takenPieceCoordinates[1]].setTaken(false);
        List<int[]> possibleNextMoves;
        if (checkedPieceType == Piece.PieceType.KING) {
            possibleNextMoves = boardClone[attemptedMove[2]][attemptedMove[3]].piece.checkLegalMoves(getQueenLogic());
        } else {
            possibleNextMoves = boardClone[attemptedMove[2]][attemptedMove[3]].piece.checkLegalMoves(getBackwardsLogic());
        }
        if (possibleNextMoves!=null) {
            for (int[] move : possibleNextMoves) {
                if (move[2]>0) {
                    goodMove = true;
                    CheckersClient.frame.successiveCaptureMode = true;
                    CheckersClient.frame.successiveX=attemptedMove[2];
                    CheckersClient.frame.successiveY=attemptedMove[3];
                    break;
                }
            }
        }
        if (!goodMove) {
            clearSuccessive();
        }

    }
    private void clearSuccessive() {
        CheckersClient.frame.successiveCaptureMode = false;
        CheckersClient.frame.successiveX=-1;
        CheckersClient.frame.successiveY=-1;
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
