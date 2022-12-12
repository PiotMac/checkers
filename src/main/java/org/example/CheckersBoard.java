package org.example;

public class CheckersBoard {

    final static private int lim = 10; //rozmiar szachownicy
    public static Square[][] board = new Square[lim][lim];
    public void createBoard() {
        for (int i = 0; i < lim; i++) {
            if (i % 2 == 0 && i != 4) {
                for (int j = 0; j < lim; j++) {
                    if (j % 2 != 0) {
                        board[i][j] = new Square(true, lim);
                    }
                    else {
                        board[i][j] = new Square(false, lim);
                    }
                    board[i][j].setCoordinates(i, j);
                }
            }
            else if (i % 2 != 0 && i != 5){
                for (int j = 0; j < lim; j++) {
                    if (j % 2 == 0) {
                        board[i][j] = new Square(true, lim);
                    }
                    else {
                        board[i][j] = new Square(false, lim);
                    }
                    board[i][j].setCoordinates(i, j);
                }
            }
            else {
                for (int j = 0; j < lim; j++) {
                    board[i][j] = new Square(false, lim);
                    board[i][j].setCoordinates(i, j);
                }
            }
        }
        printBoard();
    }
    private void printBoard() {
        for (int i = 0; i < lim; i++) {
            for (int j = 0; j < lim; j++) {
                //TODO: Add Square.isTaken().getTeam() method
                if (board[i][j].isTaken() && i <= 3) {
                    System.out.print("| X | ");
                }
                else if (board[i][j].isTaken() && i >= 6) {
                    System.out.print("| O | ");
                }
                else {
                    System.out.print("|---| ");
                }
            }
            System.out.println();
        }
    }
}
