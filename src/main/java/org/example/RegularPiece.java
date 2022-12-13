package org.example;

public class RegularPiece implements Piece{
    private Team team;
    private Team opposingTeam;
    Square currentSquare;
    private final Square[] neighbours = {
            currentSquare.getTopLeftNeighbour(),
            currentSquare.getTopRightNeighbour(),
            currentSquare.getBottomLeftNeighbour(),
            currentSquare.getBottomRightNeighbour()
    };


    public void checkLegalMoves() {
        /*
            check neighbouring squares
            backwards (bottom for white, top for black):
                if taken by same team or empty: do nothing
                if taken by enemy:
                    if square in same direction behind it is empty
                        increment captureInMove by 1
                        ??create new board that with a hypothetical move
                        ??check if there are any more available captures
                    else: do nothing
            forwards (bottom for black, top for white):
                if empty:
                    make move available if no captures available
                if taken by same team:
                    do nothing
                if taken by enemy team:
                    check for capture behind
                    (same procedure as in ??)

         */

        for (int i = 0; i<4; i++) {
            if (!neighbours[i].isTaken()) {
                System.out.println("You can go to ");
                neighbours[i].getCoordinates();
            }
        }
    }
    public void setWhite(){
        this.team = Team.WHITE;
        this.opposingTeam = Team.BLACK;
    }
    public void setBlack() {
        this.team = Team.BLACK;
        this.opposingTeam = Team.WHITE;
    }
    public Team getTeam() {
        return this.team;
    }
}
