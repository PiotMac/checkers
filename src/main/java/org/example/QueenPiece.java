package org.example;

import java.util.ArrayList;
import java.util.List;

public class QueenPiece extends PieceClass{
    private final int PieceTypeId = 1;
    private Square[] neighbours;
    private Square currentSquare;
    @Override
    public void setCurrentSquare(Square square) {
        this.currentSquare = square;
        this.neighbours = square.getNeighbours();
    }
    private int[] getCaptureMoves(Square currentSquare, int direction) {
        Square squareBehind = currentSquare.getNeighbours()[direction];
        while (squareBehind!=null) {
            if (squareBehind.isTaken() && squareBehind.getTeam()!=this.getTeam()) {
                Square squareOneBack = squareBehind.getNeighbours()[direction];
                if (squareOneBack != null && !squareOneBack.isTaken()) {
                    return new int[]{squareOneBack.x, squareOneBack.y, 1, this.PieceTypeId};
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
                listToAdd.add(new int[]{squareBehind.x, squareBehind.y, 0, this.PieceTypeId});
            }
            squareBehind = squareBehind.getNeighbours()[direction];
        }
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
    public List<int[]> checkLegalMoves(boolean functionality) {
        //functionality: whether it can move for multiple squares at once
        List<int[]> nonCaptureMoves = new ArrayList<>();
        List<int[]> captureMoves = new ArrayList<>();
        if (functionality) {
            for (int i=0; i<4; i++) {
                if (getCaptureMoves(this.currentSquare,i)!=null) {
                    captureMoves.add(getCaptureMoves(this.currentSquare,i));
                }
            }
            if (captureMoves.isEmpty()) {
                for (int i=0; i<4; i++) {
                    getNonCaptures(this.currentSquare, i, nonCaptureMoves);
                }
            }
        } else {
            for (int i = 0; i<4; i++) {
                if (neighbours[i]!=null){
                    if (checkCapture(neighbours[i],i)!=null) {
                        captureMoves.add(checkCapture(neighbours[i], i));
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
    @Override
    public PieceType getPieceType() {return PieceType.KING;}
}
