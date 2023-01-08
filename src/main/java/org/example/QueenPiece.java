package org.example;

import java.util.ArrayList;
import java.util.List;

public class QueenPiece extends PieceClass{
    private Team team;
    public int x;
    public int y;
    private Square currentSquare;

    @Override
    public void setCurrentSquare(Square square) {
        this.currentSquare = square;
    }
    private int[] getCaptureMoves(Square currentSquare, int direction) {
        Square squareBehind = currentSquare.getNeighbours()[direction];
        while (squareBehind!=null) {
            if (squareBehind.isTaken() && squareBehind.getTeam()!=this.getTeam()) {
                Square squareOneBack = squareBehind.getNeighbours()[direction];
                if (squareOneBack != null && !squareOneBack.isTaken()) {
                    return new int[]{squareOneBack.x, squareOneBack.y, 1, squareBehind.x, squareBehind.y};
                }
            } else if (squareBehind.isTaken() && squareBehind.getTeam()==this.getTeam()) {
                return null;
            }
            squareBehind = squareBehind.getNeighbours()[direction];
        }
        return null;
    }
    private void getNonCaptures(Square currentSquare, int direction, List<int[]> listToAdd) {
        Square squareBehind = currentSquare.getNeighbours()[direction];
        while (squareBehind!=null) {
            if (!squareBehind.isTaken()) {
                listToAdd.add(new int[]{squareBehind.x, squareBehind.y, 0, -1,-1});
            }
            squareBehind = squareBehind.getNeighbours()[direction];
        }
    }


    @Override
    public List<int[]> checkLegalMoves() {
        boolean captureAvailable = false;
        List<int[]> possibleMoves = new ArrayList<>();
        for (int i = 0; i<4; i++) {
            if (getCaptureMoves(this.currentSquare, i)!=null) {
                captureAvailable = true;
                possibleMoves.add(getCaptureMoves(this.currentSquare, i));
            }
        }
        if (!captureAvailable) {
            for (int i = 0; i < 4; i++) {
                getNonCaptures(this.currentSquare, i, possibleMoves);
            }
        }
        return possibleMoves;
    }
}
