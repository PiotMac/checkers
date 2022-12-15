package org.example;

public class CheckersBoard {

    private static int lim; //rozmiar szachownicy
    public static Square[][] board;
    private int id = 1;
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
            if (i % 2 == 0) {
                for (int j = 0; j < lim; j++) {
                    if (j % 2 != 0) {
                        board[i][j] = new Square(lim);
                        board[i][j].setAsPlayable();
                        board[i][j].setId(id);
                        board[i][j].setCoordinates(i, j);
                        id++;
                    }
                    else {
                        board[i][j] = null;
                    }
                }
            }
            else {
                for (int j = 0; j < lim; j++) {
                    if (j % 2 == 0) {
                        board[i][j] = new Square(lim);
                        board[i][j].setAsPlayable();
                        board[i][j].setId(id);
                        board[i][j].setCoordinates(i, j);
                        id++;
                    }
                    else {
                        board[i][j] = null;
                    }
                }
            }
        }
        //For every playable square set neighbours
        for (int i = 0; i < lim; i++) {
            for (int j = 0; j < lim; j++) {
                if (board[i][j] != null) {
                    board[i][j].setNeighbours();
                }
            }
        }
        //Set starting pieces
        for (int i = 0; i < lim; i++) {
            for (int j = 0; j < lim; j++) {
                if (board[i][j] != null && i < lim/2 - 1) {
                    board[i][j].setPiece(Piece.Team.WHITE);
                    board[i][j].piece.setCurrentSquareId(board[i][j].getId());
                }
                else if (board[i][j] != null && i >= lim/2 + 1) {
                    board[i][j].setPiece(Piece.Team.BLACK);
                    board[i][j].piece.setCurrentSquareId(board[i][j].getId());
                }
            }
        }
    }
}
