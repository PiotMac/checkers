package org.example;

import java.util.ArrayList;

public class Square {
    private final boolean isTaken;
    public int x;
    public int y;
    public Square[] neighbours = new Square[4];
    ArrayList<Square> diagonal;
    public Square(boolean isTaken) {
        this.isTaken = isTaken;

    }
    public boolean isTaken() {
        return isTaken;
    }
    public void setNeighbours() {
        setTopLeftNeighbour();
        setTopRightNeighbour();
        setBottomLeftNeighbour();
        setBottomRightNeighbour();
    }

    private void setBottomRightNeighbour() {
        if (x == 9 || y == 9) {
            neighbours[3] = null;
        }
        else {
            neighbours[3] = CheckersBoard.board[x + 1][y + 1];
        }
    }

    private void setBottomLeftNeighbour() {
        if (x == 9 || y == 0) {
            neighbours[2] = null;
        }
        else {
            neighbours[2] = CheckersBoard.board[x + 1][y - 1];
        }
    }

    private void setTopRightNeighbour() {
        if (x == 0 || y == 9) {
            neighbours[1] = null;
        }
        else {
            neighbours[1] = CheckersBoard.board[x - 1][y + 1];
        }
    }

    private void setTopLeftNeighbour() {
        if (x == 0 || y == 0) {
            neighbours[0] = null;
        }
        else {
            neighbours[0] = CheckersBoard.board[x - 1][y - 1];
        }
    }

    public Square[] getNeighbours() {
        System.out.println(x);
        System.out.println(y);
        System.out.println("0: " + neighbours[0]);
        System.out.println("1: " + neighbours[1]);
        System.out.println("2: " + neighbours[2]);
        System.out.println("3: " + neighbours[3]);
        return neighbours;
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
