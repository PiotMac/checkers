package org.example;


public class Square {
    private boolean isTaken = false;
    public int x;
    public int y;
    final private int limit;
    private final Square[] neighbours = new Square[4];
    public Piece piece;

    public Square(int limit) {
        this.limit = limit;
    }

    public void setPiece(Piece.Team team, Piece.PieceType pieceType) {
        if (pieceType== Piece.PieceType.MAN) {
            piece = new RegularPiece();
        } else if (pieceType == Piece.PieceType.KING) {
            piece = new QueenPiece();
        }
        piece.setCurrentSquare(this);
        if (team == Piece.Team.WHITE) {
            piece.setWhite();
        }
        else {
            piece.setBlack();
        }
        setTaken(true);
    }

    public void setTaken(boolean truth) {
        isTaken = truth;
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

    public void setCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
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

    public int getX() {
        return this.x;
    }
    public int getY() {
        return this.y;
    }
}
