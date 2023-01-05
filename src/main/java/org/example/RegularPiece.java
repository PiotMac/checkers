package org.example;

import java.util.ArrayList;
import java.util.List;

public class RegularPiece extends PieceClass{
    private Square[] neighbours;
    @Override
    public void setCurrentSquare(Square square) {
        this.neighbours = square.getNeighbours();
    }

    private int[] checkCapture(Square sq, int direction) {
        if (sq.isTaken() && sq.getTeam()!=this.getTeam()){
            Square squareBehind = sq.getNeighbours()[direction];
            if (squareBehind!=null && !squareBehind.isTaken()) {
                return new int[]{squareBehind.getX(),squareBehind.getY(), 1, sq.getX(), sq.getY()};//final squareX, final squareY, captureAvailable, jumped squareX, jumped squareY
            }
        }
        return null;
    }
    @Override
    public List<int[]> checkLegalMoves() {
        boolean captureAvailable = false;
        List<int[]> possibleMoves = new ArrayList<>();
        for (int i : this.getBackwardIds()) {
            if (neighbours[i]!=null) {
                if (checkCapture(neighbours[i],i)!=null) {
                    possibleMoves.add(checkCapture(neighbours[i], i));
                    captureAvailable = true;
                }
            }
        }
        for (int i : this.getForwardIds()) {
            if (neighbours[i]!=null){
                if (checkCapture(neighbours[i],i)!=null) {
                    possibleMoves.add(checkCapture(neighbours[i], i));
                    captureAvailable = true;
                }
                else if (!neighbours[i].isTaken()) {
                    int[] possibleMoveIds = {neighbours[i].getX(),neighbours[i].getY(), 0, -1, -1};
                    possibleMoves.add(possibleMoveIds);
                }
            }
        }

        if (possibleMoves.size()>0) {
            if (captureAvailable) {
                List<int[]> legalMoves = new ArrayList<>();
                for (int[] move : possibleMoves) {
                    if (move[2]==1) {
                        legalMoves.add(move);
                    }
                }
                return legalMoves;
            }
            else {
                return possibleMoves;
            }
        }
        else {
            return null;
        }
    }
}
