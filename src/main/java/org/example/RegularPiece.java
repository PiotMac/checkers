package org.example;

import java.util.ArrayList;
import java.util.List;

public class RegularPiece extends PieceClass {
    private Square[] neighbours;
    private final int PieceTypeId = 0;
    @Override
    public void setCurrentSquare(Square square) {
        this.neighbours = square.getNeighbours();
    }
    private int[] checkCapture(Square sq, int direction) {
        if (sq.isTaken() && sq.getTeam()!=this.getTeam()){
            Square squareBehind = sq.getNeighbours()[direction];
            if (squareBehind!=null && !squareBehind.isTaken()) {
                return new int[]{squareBehind.getX(),squareBehind.getY(), 1,  this.PieceTypeId};//final squareX, final squareY, captureAvailable, jumped squareX, jumped squareY
            }
        }
        return null;
    }
    @Override
    public List<int[]> checkLegalMoves() {
        List<int[]> nonCaptureMoves = new ArrayList<>();
        List<int[]> captureMoves = new ArrayList<>();
        for (int i : this.getForwardIds()) {
            if (neighbours[i]!=null){
                if (checkCapture(neighbours[i],i)!=null) {
                    captureMoves.add(checkCapture(neighbours[i], i));
                }
                else if (!neighbours[i].isTaken()) {
                    nonCaptureMoves.add(new int[] {neighbours[i].getX(), neighbours[i].getY(), 0, this.PieceTypeId});
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
    @Override
    public PieceType getPieceType() {return PieceType.MAN;}
}
