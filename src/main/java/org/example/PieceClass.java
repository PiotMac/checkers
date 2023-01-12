package org.example;

import java.util.List;

abstract class PieceClass implements Piece{

    private Piece.Team team;


    public void setWhite(){
        this.team = Team.WHITE;
    }
    public void setBlack(){
        this.team = Team.BLACK;
    }
    public Team getTeam() {
        return this.team;
    }

    public int[] getForwardIds() {
        if (this.team == Team.WHITE) {
            return new int[]{2, 3};
        }
        else {
            return new int[]{0,1};
        }
    }
}
