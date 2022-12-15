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
    private int currentSquareId;

    private void checkCapture(Square sq, int direction, List<Integer> pc, List<Integer> pm) {
        if (sq.isTaken() && sq.getTeam()==this.opposingTeam){
            Square squareBehind = sq.getNeighbours()[direction];
            if (!squareBehind.isTaken()) {
                pm.add(squareBehind.getId());
                pc.add(1);
                System.out.println("Capture possible"); //placeholder
                //create new board in same state as current state
                //make hypothetical move
                //check for more captures
            }
        }
    }
    @Override
    public void checkLegalMoves() {
        List<Integer> possibleCaptures = new ArrayList<>();
        List<Integer> possibleMoves = new ArrayList<>();
        for (int i : this.getBackwardIds()) {
            if (neighbours[i]!=null) {
                checkCapture(neighbours[i], i, possibleCaptures, possibleMoves);
            }
        }
        for (int i : this.getForwardIds()) {
            if (neighbours[i]!=null){
                if (!neighbours[i].isTaken()) {
                    //make available if no capture opportunities available
                    possibleMoves.add(neighbours[i].getId());
                    possibleCaptures.add(0);
                }
                checkCapture(neighbours[i],i, possibleCaptures, possibleMoves);
            }
        }

        if (possibleMoves.size()>0) {
            for (int i = 0; i < possibleMoves.size(); i++) {
                if (possibleCaptures.get(i).equals(Collections.max(possibleCaptures))) {
                    System.out.println("Move possible for piece on square "+ this.currentSquareId+": "+possibleMoves.get(i));
                }
            }
        }
        else {
            System.out.println("No move possible for piece on square "+this.currentSquareId);
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
    public void setCurrentSquareId(int id) {
        this.currentSquareId = id;
    }
}
