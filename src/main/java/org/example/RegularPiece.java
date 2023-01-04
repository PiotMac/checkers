package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RegularPiece implements Piece{
    private Team team;
    private Team opposingTeam;
    public int x;
    public int y;
    private Square[] neighbours;


    private int[] checkCapture(Square sq, int direction) {
        if (sq.isTaken() && sq.getTeam()==this.opposingTeam){
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
            //System.out.println("No move possible for piece on square ["+ this.x +", "+this.y+"]");
            return null;
        }

    }
    @Override
    public void setWhite(){
        this.team = Team.WHITE;
        this.opposingTeam = Team.BLACK;
    }
    @Override
    public void setBlack() {
        this.team = Team.BLACK;
        this.opposingTeam = Team.WHITE;
    }

    @Override
    public void setCurrentNeighbours(Square[] neighbours) {
        this.neighbours = neighbours;
    }

    @Override
    public Team getTeam() {
        return this.team;
    }
    @Override
    public void setCurrentCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }
    @Override
    public int[] getForwardIds() {
        if (this.team == Team.WHITE) {
            return new int[]{2, 3};
        }
        else {
            return new int[]{0,1};
        }
    }
    @Override
    public int[] getBackwardIds() {
        if (this.team == Team.WHITE) {
            return new int[]{0,1};
        }
        else {
            return new int[]{2,3};
        }
    }
}
