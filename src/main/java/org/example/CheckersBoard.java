package org.example;

public class CheckersBoard {
    public static Square[][] board = new Square[10][10];
    public void createBoard() {
        for (int i = 0; i <= 9; i++) {
            if (i % 2 == 0 && i != 4) {
                for (int j = 0; j <= 9; j++) {
                    if (j % 2 != 0) {
                        board[i][j] = new Square(true);
                        board[i][j].setCoordinates(i, j);
                        board[i][j].setNeighbours();
                    }
                    else {
                        board[i][j] = new Square(false);
                        board[i][j].setCoordinates(i, j);
                        board[i][j].setNeighbours();
                    }
                }
            }
            else if (i % 2 != 0 && i != 5){
                for (int j = 0; j <= 9; j++) {
                    if (j % 2 == 0) {
                        board[i][j] = new Square(true);
                        board[i][j].setCoordinates(i, j);
                        board[i][j].setNeighbours();
                    }
                    else {
                        board[i][j] = new Square(false);
                        board[i][j].setCoordinates(i, j);
                        board[i][j].setNeighbours();
                    }
                }
            }
            else {
                for (int j = 0; j <= 9; j++) {
                    board[i][j] = new Square(false);
                    board[i][j].setCoordinates(i, j);
                    board[i][j].setNeighbours();
                }
            }
        }
        printBoard();
    }
    private void printBoard() {
        for (int i = 0; i <= 9; i++) {
            for (int j = 0; j <= 9; j++) {
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
