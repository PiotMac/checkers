package org.example;

public class RegularPiece implements Piece{
    private Team team;
    private Team opposingTeam;
    public int x;
    public int y;
    private Square[] neighbours;

    @Override
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
            if (neighbours[i] != null && !neighbours[i].isTaken()) {
                System.out.println("Piece at coordinates " + x + " and " + y + " can go to square: " + neighbours[i].id);
                neighbours[i].getCoordinates();
            }
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
}
