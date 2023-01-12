package org.example;

import net.CheckersClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasa warcabów angielskich
 */
public class EnglishCheckersBoard extends CheckersBoard {
    // Króle poruszają się o jedno pole
    private static final boolean kingsMovesOnDiagonals = false;
    // Pionki nie mogą ruszać się do tyłu
    private static final boolean movingBackwards = false;

    /**
     * Konstruktor angielskich warcabów
     */
    public EnglishCheckersBoard() {
        lim = 8;
        rows = 3;
        board = new Square[lim][lim];
    }

    /**
     * Metoda mówiąca jak mogą poruszać się króle
     * @return - zwraca informację, czy król może poruszać się po całych przekątnych
     */
    @Override
    public boolean getKingLogic() {
        return kingsMovesOnDiagonals;
    }

    /**
     * Metoda mówiąca jak mogą poruszać się pionki
     * @return - zwraca informację, czy pionki mogą poruszać się do tyłu
     */
    @Override
    public boolean getBackwardsLogic() {
        return movingBackwards;
    }

    /**
     *
     * @param firstX - pierwsza współrzędna pierwszego kliknięcia
     * @param firstY - druga współrzędna pierwszego kliknięcia
     * @param secondX - pierwsza współrzędna drugiego kliknięcia
     * @param secondY - druga współrzędna pierwszego kliknięcia
     * @param yourMove - parametr sprawdzający od kogo jest ruch
     * @param successiveCapMode - parametr sprawdzający, czy włączony jest tryb wielokrotnego bicia
     * @return - lista współrzędnych zaktualizowanych pól
     */
    @Override
    public List<int[]> updateMove(int firstX, int firstY, int secondX, int secondY, boolean yourMove, boolean successiveCapMode) {
        List<int[]> squaresToUpdate = new ArrayList<>();
        board[firstX][firstY].setTaken(false);
        squaresToUpdate.add(new int[] {firstX, firstY});
        squaresToUpdate.add(new int[] {secondX, secondY});
        boolean addedKing = false;
        int[] potentiallyJumped = getJumpedPieceCoordinates(firstX, firstY, secondX, secondY);
        if (potentiallyJumped!=null) {
            board[potentiallyJumped[0]][potentiallyJumped[1]].setTaken(false);
            squaresToUpdate.add(potentiallyJumped);
        }

        if (secondX==lim-1) {
            if (CheckersClient.frame.thisPlayerTeam == Piece.Team.WHITE && yourMove) {
                board[secondX][secondY].setPiece(CheckersClient.frame.thisPlayerTeam, Piece.PieceType.KING);
            } else if (CheckersClient.frame.thisPlayerTeam == Piece.Team.BLACK && !yourMove) {
                board[secondX][secondY].setPiece(CheckersClient.frame.anotherPlayerTeam, Piece.PieceType.KING);
            }
            clearSuccessive();
            successiveCapMode=false;
            addedKing = true;
        }
        if (secondX==0) {
            if (CheckersClient.frame.thisPlayerTeam == Piece.Team.BLACK && yourMove) {
                board[secondX][secondY].setPiece(CheckersClient.frame.thisPlayerTeam, Piece.PieceType.KING);
            }
            else if (CheckersClient.frame.thisPlayerTeam == Piece.Team.WHITE && !yourMove) {
                board[secondX][secondY].setPiece(CheckersClient.frame.anotherPlayerTeam, Piece.PieceType.KING);
            }
            clearSuccessive();
            successiveCapMode=false;
            addedKing = true;
        }
        if(!successiveCapMode) {
            if (yourMove) {
                if (!addedKing) {
                    board[secondX][secondY].setPiece(CheckersClient.frame.thisPlayerTeam, CheckersClient.frame.attemptedMovedPieceType);
                }
                CheckersClient.frame.setTitle("Opponent's turn!");
                System.out.println("Successful move!");
            }
            else {
                if (!addedKing) {
                    board[secondX][secondY].setPiece(CheckersClient.frame.anotherPlayerTeam, CheckersClient.frame.attemptedMovedPieceType);
                }
                CheckersClient.frame.setTitle("Your turn!");
                System.out.println("Opponent made a move");
            }

        }
        else {
            if (yourMove) {
                board[secondX][secondY].setPiece(CheckersClient.frame.thisPlayerTeam, CheckersClient.frame.attemptedMovedPieceType);
                System.out.println("Successive capture possible!");
            }
            else {
                board[secondX][secondY].setPiece(CheckersClient.frame.anotherPlayerTeam, CheckersClient.frame.attemptedMovedPieceType);
                System.out.println("You fell victim to a successive capture");
            }

        }
        return squaresToUpdate;
    }

    /**
     * Metoda sprawdzająca, czy możliwe jest wielokrotne bicie
     * @param attemptedMove - współrzędne wykonanego ruchu
     */
    @Override
    public void isSuccessiveCaptureAvailable(int[] attemptedMove) {
        Square[][] boardClone = CheckersBoard.board.clone();
        boardClone[attemptedMove[0]][attemptedMove[1]].setTaken(false);
        boardClone[attemptedMove[2]][attemptedMove[3]].setPiece(CheckersClient.frame.thisPlayerTeam, CheckersClient.frame.attemptedMovedPieceType);
        CheckersClient.frame.successiveJumpedXYs.add(getJumpedPieceCoordinates(attemptedMove[0],attemptedMove[1],attemptedMove[2],attemptedMove[3]));
        List<int[]> possibleNextMoves;
        if (CheckersClient.frame.attemptedMovedPieceType==Piece.PieceType.KING) {
            possibleNextMoves = boardClone[attemptedMove[2]][attemptedMove[3]].piece.checkLegalMoves(kingsMovesOnDiagonals);
        } else {
            possibleNextMoves = boardClone[attemptedMove[2]][attemptedMove[3]].piece.checkLegalMoves(movingBackwards);
        }
        successfulCaptureChanges(attemptedMove, possibleNextMoves);
    }

}
