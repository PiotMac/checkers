package org.example;

/**
 * Klasa pola na planszy
 */
public class Square {
    private boolean isTaken = false;
    public int x;
    public int y;
    final private int limit;
    private final Square[] neighbours = new Square[4];
    public Piece piece;

    /**
     * Konstruktor klasy pole
     * @param limit - rozmiar planszy
     */
    public Square(int limit) {
        this.limit = limit;
    }

    /**
     * Metoda tworząca bierkę na tym polu
     * @param team - kolor bierki
     * @param pieceType - typ bierki
     */
    public void setPiece(Piece.Team team, Piece.PieceType pieceType) {
        if (pieceType== Piece.PieceType.MAN) {
            piece = new RegularPiece();
        } else if (pieceType == Piece.PieceType.KING) {
            piece = new KingPiece();
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

    /**
     * Metoda, która ustawia, czy to pole jest zajęte
     * @param truth - czy coś znajduje się na polu
     */
    public void setTaken(boolean truth) {
        isTaken = truth;
    }

    /**
     * Metoda zwracająca informację, czy coś znajduje się na tym polu
     * @return - czy coś znajduje się na polu
     */
    public boolean isTaken() {
        return isTaken;
    }

    /**
     * Metoda zwracająca prawego dolnego sąsiada tego pola
     * @return - prawy dolny sąsiad
     */
    public Square getBottomRightNeighbour() {
        if (this.x == limit-1 || this.y == limit-1) {
            return null;
        }
        else {
            return CheckersBoard.board[x + 1][y + 1];
        }
    }

    /**
     * Metoda zwracająca lewego dolnego sąsiada tego pola
     * @return - lewy dolny sąsiad
     */
    public Square getBottomLeftNeighbour() {
        if (this.x == limit - 1 || this.y == 0) {
            return null;
        }
        else {
            return CheckersBoard.board[x + 1][y - 1];
        }
    }

    /**
     * Metoda zwracająca prawego górnego sąsiada tego pola
     * @return - prawy górny sąsiad
     */
    public Square getTopRightNeighbour() {
        if (this.x == 0 || this.y == limit - 1) {
            return null;
        }
        else {
            return CheckersBoard.board[x - 1][y + 1];
        }
    }

    /**
     * Metoda zwracająca lewego górnego sąsiada tego pola
     * @return - lewy górny sąsiad
     */
    public Square getTopLeftNeighbour() {
        if (this.x == 0 || this.y == 0) {
            return null;
        }
        else {
            return CheckersBoard.board[x - 1][y - 1];
        }
    }

    /**
     * Metoda ustawiająca współrzędne tego pola
     * @param x - współrzędna na osi X
     * @param y - współrzędna na osi Y
     */
    public void setCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Metoda zwracająca kolor drużyny bierki
     * @return - kolor drużyny bierki znajdującej się na tym polu
     */
    public Piece.Team getTeam() {
        return piece.getTeam();
    }

    /**
     * Metoda ustawiająca sąsiadów tego pola
     */
    public void setNeighbours() {
        neighbours[0] = this.getTopLeftNeighbour();
        neighbours[1] = this.getTopRightNeighbour();
        neighbours[2] = this.getBottomLeftNeighbour();
        neighbours[3] = this.getBottomRightNeighbour();
    }

    /**
     * Metoda zwracająca wszystkich sąsiadów tego pola
     * @return - sąsiedzi pola
     */
    public Square[] getNeighbours() {
        return neighbours;
    }

    /**
     * Metoda zwracająca pierwszą współrzędną tego pola
     * @return - współrzędna X pola
     */
    public int getX() {
        return this.x;
    }

    /**
     * Metoda zwracająca drugą współrzędną tego pola
     * @return - współrzędna Y pola
     */
    public int getY() {
        return this.y;
    }
}
