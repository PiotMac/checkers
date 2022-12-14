package org.example;

public class CheckersBoard {

    final static private int lim = 10; //rozmiar szachownicy
    public static Square[][] board = new Square[lim][lim];
    private int id = 1;
    public void createBoard() {
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
        printBoard();
    }
    private void printBoard() {
        for (int i = 0; i < lim; i++) {
            for (int j = 0; j < lim; j++) {
                if (board[i][j] == null) {
                    System.out.print("|❌| ");
                }
                else if (!(board[i][j].isTaken())){
                    System.out.print("|\uD83D\uDFEB| ");
                }
                else if (board[i][j].isTaken() && board[i][j].getTeam() == Piece.Team.BLACK) {
                    System.out.print("|⚫| ");
                }
                else if (board[i][j].isTaken() && board[i][j].getTeam() == Piece.Team.WHITE) {
                    System.out.print("|⚪| ");
                }
            }
            System.out.println();
        }
    }
}
