package org.example;

import java.util.ArrayList;

public class Square {
    private final boolean isTaken;
    public int x;
    public int y;

    final private int limit;

    ArrayList<Square> diagonal;
    public Square(boolean isTaken, int limit) {
        this.isTaken = isTaken;
        this.limit = limit;
    }
    public boolean isTaken() {
        return isTaken;
    }

    public Square getBottomRightNeighbour() {
        if (this.x == limit-1 || this.y == limit-1) {
            return null;
        }
        else {
            return CheckersBoard.board[x + 1][y + 1];
        }
    }

    public Square getBottomLeftNeighbour() {
        if (this.x == 0 || this.y == limit-1) {
            return null;
        }
        else {
            return CheckersBoard.board[x - 1][y + 1];
        }
    }

    public Square getTopRightNeighbour() {
        if (this.x == limit-1 || this.y == 0) {
            return null;
        }
        else {
            return CheckersBoard.board[x + 1][y - 1];
        }
    }

    public Square getTopLeftNeighbour() {
        if (this.x == 0 || this.y == 0) {
            return null;
        }
        else {
            return CheckersBoard.board[x - 1][y - 1];
        }
    }

    private void getTopLeftDiagonal() {

    }
    private void getTopRightDiagonal() {

    }
    private void getBottomLeftDiagonal() {

    }
    private void getBottomRightDiagonal() {

    }

    public void setCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public void getCoordinates() {
        System.out.println("X:= " + x);
        System.out.println("Y:= " + y);
    }
}
