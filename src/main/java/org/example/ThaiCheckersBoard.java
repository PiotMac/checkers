package org.example;

import net.CheckersClient;

import java.util.List;

/**
 * Klasa warcabów tajskich
 */
public class ThaiCheckersBoard extends CheckersBoard{

    private static final boolean kingMovesOnDiagonals = true;
    private static final boolean movingBackwards = false;

    /**
     * Konstruktor tajskich warcabów
     */
    public ThaiCheckersBoard() {
        lim = 8;
        rows = 2;
        board = new Square[lim][lim];
    }

    /**
     * Metoda mówiąca jak mogą poruszać się króle
     * @return - zwraca informację, czy król może poruszać się po całych przekątnych
     */
    @Override
    public boolean getKingLogic() {
        return kingMovesOnDiagonals;
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
     * Metoda sprawdzająca, czy możliwe jest wielokrotne bicie
     * @param attemptedMove - współrzędne wykonanego ruchu
     */
    @Override
    public void isSuccessiveCaptureAvailable(int[] attemptedMove) {
        Square[][] boardClone = CheckersBoard.board.clone();
        Piece.PieceType checkedPieceType;
        boardClone[attemptedMove[0]][attemptedMove[1]].setTaken(false);
        if (attemptedMove[2]==0 && CheckersClient.frame.thisPlayerTeam== Piece.Team.BLACK) {
            boardClone[attemptedMove[2]][attemptedMove[3]].setPiece(CheckersClient.frame.thisPlayerTeam, Piece.PieceType.KING);
            checkedPieceType = Piece.PieceType.KING;
        } else if (attemptedMove[2]==lim-1 && CheckersClient.frame.thisPlayerTeam== Piece.Team.WHITE) {
            boardClone[attemptedMove[2]][attemptedMove[3]].setPiece(CheckersClient.frame.thisPlayerTeam, Piece.PieceType.KING);
            checkedPieceType = Piece.PieceType.KING;
        } else {
            boardClone[attemptedMove[2]][attemptedMove[3]].setPiece(CheckersClient.frame.thisPlayerTeam, CheckersClient.frame.attemptedMovedPieceType);
            checkedPieceType = CheckersClient.frame.attemptedMovedPieceType;
        }
        int[] takenPieceCoordinates = getJumpedPieceCoordinates(attemptedMove[0], attemptedMove[1], attemptedMove[2], attemptedMove[3]);
        boardClone[takenPieceCoordinates[0]][takenPieceCoordinates[1]].setTaken(false);
        List<int[]> possibleNextMoves;
        if (checkedPieceType == Piece.PieceType.KING) {
            possibleNextMoves = boardClone[attemptedMove[2]][attemptedMove[3]].piece.checkLegalMoves(getKingLogic());
        } else {
            possibleNextMoves = boardClone[attemptedMove[2]][attemptedMove[3]].piece.checkLegalMoves(getBackwardsLogic());
        }
        successfulCaptureChanges(attemptedMove, possibleNextMoves);

    }

    /**
     * Nadpisana metoda usuwająca bierki w wielokrotnym biciu
     */
    @Override
    void clearSuccessive() {
        CheckersClient.frame.successiveCaptureMode = false;
        CheckersClient.frame.successiveX=-1;
        CheckersClient.frame.successiveY=-1;
    }

}
