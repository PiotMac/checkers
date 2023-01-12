import org.example.*;
import org.junit.Assert;
import org.junit.Test;

public class ShashkiMoveAvailabilityTest {
    CheckersBoard checkersBoard = new ShashkiCheckersBoard();
    private void clearBoard(Square[][] board) {
        for (int i = 0; i<checkersBoard.getSize(); i++) {
            for (int j=0; j<checkersBoard.getSize(); j++) {
                if ((i+j)%2==1) {
                    board[i][j].setTaken(false);
                }
            }
        }
    }
    @Test
    public void checkBackwardsCapture() {
        Square[][] board = checkersBoard.getBoard();
        clearBoard(board);
        board[3][2].setPiece(Piece.Team.BLACK, Piece.PieceType.MAN);
        board[4][3].setPiece(Piece.Team.WHITE, Piece.PieceType.MAN);
        Assert.assertEquals(board[3][2].piece.checkLegalMoves(checkersBoard.getBackwardsLogic()).size(),1);
        Assert.assertArrayEquals(board[3][2].piece.checkLegalMoves(checkersBoard.getBackwardsLogic()).get(0), new int[]{5,4,1,0});
    }
    @Test
    public void checkKingCapture() {
        Square[][] board = checkersBoard.getBoard();
        clearBoard(board);
        board[0][7].setPiece(Piece.Team.BLACK, Piece.PieceType.KING);
        board[1][6].setPiece(Piece.Team.WHITE, Piece.PieceType.MAN);
        board[2][5].setPiece(Piece.Team.BLACK, Piece.PieceType.MAN);
        Assert.assertNull(board[0][7].piece.checkLegalMoves(checkersBoard.getKingLogic()));
    }
}
