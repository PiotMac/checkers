package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasa pionka
 */
public class RegularPiece extends PieceClass {
    private Square[] neighbours;
    private final int PieceTypeId = 0;

    /**
     * Metoda ustawiająca aktualne pole oraz przydzielająca sąsiadów dla danego pionka
     * @param square - pole, na którym znajduje się pionek
     */
    @Override
    public void setCurrentSquare(Square square) {
        this.neighbours = square.getNeighbours();
    }

    /**
     * Metoda sprawdzająca listę możliwych ruchów
     * @param functionality - określa, czy pionek może poruszać się do tyłu
     * @return - lista dozwolonych ruchów
     */
    @Override
    public List<int[]> checkLegalMoves(boolean functionality) {
        List<int[]> nonCaptureMoves = new ArrayList<>();
        List<int[]> captureMoves = new ArrayList<>();
        if (functionality) {
            for (int i = 0; i<4; i++) {
                addToLists(nonCaptureMoves, captureMoves, i);
            }
        } else {
            for (int i : this.getForwardIds()) {
                addToLists(nonCaptureMoves, captureMoves, i);
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
     * Metoda dodająca legalne ruchy do listy dozwolonych posunięć
     * @param nonCaptureMoves - lista ruchów bez bicia
     * @param captureMoves - lista ruchów z biciem
     * @param i - kierunek bicia
     */
    private void addToLists(List<int[]> nonCaptureMoves, List<int[]> captureMoves, int i) {
        if (neighbours[i]!=null){
            if (checkCapture(neighbours[i],i, this.PieceTypeId)!=null) {
                captureMoves.add(checkCapture(neighbours[i], i, this.PieceTypeId));
            }
            else if (!neighbours[i].isTaken() && (i==getForwardIds()[0] || i==getForwardIds()[1])) {
                nonCaptureMoves.add(new int[] {neighbours[i].getX(), neighbours[i].getY(), 0, this.PieceTypeId});
            }
        }
    }

    /**
     * Metoda zwracająca rodzaj bierki
     * @return - typ bierki
     */
    @Override
    public PieceType getPieceType() {
        return PieceType.MAN;
    }
}
