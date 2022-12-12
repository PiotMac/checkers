package org.example;

public class RegularPiece implements Piece{
    private String team="";
    Square currentSquare;
    private final Square[] neighbours = {
            currentSquare.getTopLeftNeighbour(),
            currentSquare.getTopRightNeighbour(),
            currentSquare.getBottomLeftNeighbour(),
            currentSquare.getBottomRightNeighbour()
    };


    public void checkLegalMoves() {
        for (int i = 0; i<4; i++) {
            if (!neighbours[i].isTaken()) {
                System.out.println("You can go to ");
                neighbours[i].getCoordinates();
            }
        }
    }
    public void setTeam(String team){
        this.team = team;
    }
    public String getTeam() {
        return this.team;
    }
}
