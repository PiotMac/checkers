package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasa króla
 */
public class KingPiece extends PieceClass{
    private final int PieceTypeId = 1;
    private Square[] neighbours;
    private Square currentSquare;

    /**
     * Metoda ustawiająca aktualne pole oraz przydzielająca sąsiadów dla danego króla
     * @param square - pole, na którym znajduje się król
     */
    @Override
    public void setCurrentSquare(Square square) {
        this.currentSquare = square;
        this.neighbours = square.getNeighbours();
    }

    /**
     * Metoda zwracająca ruchy, w których król jest w stanie bić
     * @param currentSquare - aktualne pole króla
     * @param direction - kierunek przekątnej
     * @return - zwraca współrzędne ruchu bijącego w formacie {końcowa pierwsza współrzędna, końcowa druga współrzędna, flaga bicia, id typu figury}
     */
    private int[] getCaptureMoves(Square currentSquare, int direction) {
        Square squareBehind = currentSquare.getNeighbours()[direction];
        while (squareBehind!=null) {
            if (squareBehind.isTaken() && squareBehind.getTeam()!=this.getTeam()) {
                Square squareOneBack = squareBehind.getNeighbours()[direction];
                if (squareOneBack != null && !squareOneBack.isTaken()) {
                    return new int[]{squareOneBack.x, squareOneBack.y, 1, this.PieceTypeId};
                } else {
                    return null;
                }
            } else if (squareBehind.isTaken() && squareBehind.getTeam()==this.getTeam()) {
                return null;
            }
            squareBehind = squareBehind.getNeighbours()[direction];
        }
        return null;
    }

    /**
     * Metoda sprawdzająca zwykłe ruchy
     * @param currentSquare - aktualne pole króla
     * @param direction - kierunek przekątnej
     * @param listToAdd - lista legalnych ruchów
     */
    private void getNonCaptures(Square currentSquare, int direction, List<int[]> listToAdd) {
        Square squareBehind = currentSquare.getNeighbours()[direction];
        while (squareBehind!=null && !squareBehind.isTaken()) {
            listToAdd.add(new int[]{squareBehind.x, squareBehind.y, 0, this.PieceTypeId});
            squareBehind = squareBehind.getNeighbours()[direction];
        }
    }

    /**
     * Metoda sprawdzająca listę możliwych ruchów
     * @param functionality - określa, czy król może poruszać się o wiele pól
     * @return - lista dozwolonych ruchów
     */
    @Override
    public List<int[]> checkLegalMoves(boolean functionality) {
        List<int[]> nonCaptureMoves = new ArrayList<>();
        List<int[]> captureMoves = new ArrayList<>();
        if (functionality) {
            //próba znalezienia bicia
            for (int i=0; i<4; i++) {
                if (getCaptureMoves(this.currentSquare,i)!=null) {
                    captureMoves.add(getCaptureMoves(this.currentSquare,i));
                }
            }
            //jeżeli nie udało znaleźć się bicia, dodaj do listy do zwrócenia możliwe ruchy
            if (captureMoves.isEmpty()) {
                for (int i=0; i<4; i++) {
                    getNonCaptures(this.currentSquare, i, nonCaptureMoves);
                }
            }
        } else {
            for (int i = 0; i<4; i++) {
                if (neighbours[i]!=null){
                    if (checkCapture(neighbours[i],i,this.PieceTypeId)!=null) {
                        captureMoves.add(checkCapture(neighbours[i], i, this.PieceTypeId));
                    }
                    else if (!neighbours[i].isTaken()) {
                        nonCaptureMoves.add(new int[] {neighbours[i].getX(), neighbours[i].getY(), 0, this.PieceTypeId});
                    }
                }
            }
        }
        if (captureMoves.size()>0) {
            return captureMoves;
        } else if (nonCaptureMoves.size()>0) {
            return nonCaptureMoves;
        } else {
            return null;
        }
    }

    /**
     * Metoda zwracająca rodzaj bierki
     * @return - typ bierki
     */
    @Override
    public PieceType getPieceType() {
        return PieceType.KING;
    }
}
