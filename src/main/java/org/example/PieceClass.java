package org.example;

/**
 * Abstrakcyjna klasa bierek
 */
abstract class PieceClass implements Piece{
    private Piece.Team team;

    /**
     * Metoda ustawiająca drużynę na białą
     */
    public void setWhite(){
        this.team = Team.WHITE;
    }

    /**
     * Metoda ustawiająca drużynę na czarną
     */
    public void setBlack(){
        this.team = Team.BLACK;
    }

    /**
     * Metoda zwracająca kolor drużyny
     * @return - kolor drużyny
     */
    public Team getTeam() {
        return this.team;
    }

    /**
     * Metoda ustawiająca dla każdego gracza kierunek, w którym może się ruszać
     * @return - kierunek ruchu do przodu
     */
    public int[] getForwardIds() {
        if (this.team == Team.WHITE) {
            return new int[]{2, 3};
        }
        else {
            return new int[]{0,1};
        }
    }
    /**
     * Metoda sprawdzająca, czy można zbić bierkę przeciwnika
     * @param sq - pole bierki do potencjalnego zbicia
     * @param direction - kierunek, w którym następuje bicie
     * @param pieceTypeId - rodzaj bierki
     * @return - ruch umożliwiający bicie
     */
    public int[] checkCapture(Square sq, int direction, int pieceTypeId) {
        if (sq.isTaken() && sq.getTeam()!=this.getTeam()){
            Square squareBehind = sq.getNeighbours()[direction];
            if (squareBehind!=null && !squareBehind.isTaken()) {
                return new int[]{squareBehind.getX(),squareBehind.getY(), 1,  pieceTypeId};
            }
        }
        return null;
    }
}
