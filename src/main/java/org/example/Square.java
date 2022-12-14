package org.example;

import java.util.ArrayList;

public class Square {
    private boolean isTaken = false;
    private boolean isPlayable = false;
    public int id;
    public int x;
    public int y;
    final private int limit;
    private final Square[] neighbours = new Square[4];
    public RegularPiece piece;
    ArrayList<Square> diagonal;
    public Square(int limit) {
        this.limit = limit;
    }
    public void setAsPlayable() {
        isPlayable = true;
    }
    public void setPiece(Piece.Team team) {
        piece = new RegularPiece();
        piece.setCurrentCoordinates(x, y);
        piece.setCurrentNeighbours(neighbours);
        if (team == Piece.Team.WHITE) {
            piece.setWhite();
        }
        else {
            piece.setBlack();
        }
        setTaken();
    }
    public boolean isPlayable() {
        return isPlayable;
    }
    private void setTaken() {
        isTaken = true;
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
        if (this.x == limit - 1 || this.y == 0) {
            return null;
        }
        else {
            return CheckersBoard.board[x + 1][y - 1];
        }
    }

    public Square getTopRightNeighbour() {
        if (this.x == 0 || this.y == limit - 1) {
            return null;
        }
        else {
            return CheckersBoard.board[x - 1][y + 1];
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

    public Piece.Team getTeam() {
        return piece.getTeam();
    }

    public void setNeighbours() {
        neighbours[0] = this.getTopLeftNeighbour();
        neighbours[1] = this.getTopRightNeighbour();
        neighbours[2] = this.getBottomLeftNeighbour();
        neighbours[3] = this.getBottomRightNeighbour();
    }
    public Square[] getNeighbours() {
        return neighbours;
    }

    public void setId(int id) {
        this.id = id;
    }
}
