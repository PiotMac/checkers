package org.example;

public class CheckersBoard {

    private static int lim; //rozmiar szachownicy
    public static Square[][] board;

    public CheckersBoard(int limit) {
        lim = limit;
        board = new Square[lim][lim];
    }
    public int getSize() {
        return lim;
    }
    public Square[][] getBoard() {
        createBoard();
        return board;
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
                if ((i+j)%2 != 0 && i < lim/2 - 1) {
                    board[i][j].setPiece(Piece.Team.WHITE, Piece.PieceType.MAN);
                }
                else if ((i+j)%2 != 0 && i >= lim/2 + 1) {
                    board[i][j].setPiece(Piece.Team.BLACK, Piece.PieceType.MAN);
                }
            }
        }
    }

    public int[] getBoardAsArray() {
        int[] boardArray = new int[(lim*lim)/2];
        int c = 0;
        for (int i = 0; i < lim; i++) {
            for (int j=0; j < lim; j++) {
                if ((i+j)%2 != 0) {
                    if (board[i][j].isTaken() && board[i][j].piece.getTeam() == Piece.Team.WHITE) { //&& board[i][j].piece instanceof RegularPiece) {
                        boardArray[c]=-1;
                    }
                    else if (board[i][j].isTaken() && board[i][j].piece.getTeam() == Piece.Team.BLACK) {
                        boardArray[c]=1;
                    }
                    else {
                        boardArray[c]=0;
                    }
                    c=c+1;
                }
            }
        }
        return boardArray;
    }
}
