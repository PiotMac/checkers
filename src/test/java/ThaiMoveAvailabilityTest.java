import org.example.CheckersBoard;
import org.example.Piece;
import org.example.Square;
import org.example.ThaiCheckersBoard;
import org.junit.Assert;
import org.junit.Test;

public class ThaiMoveAvailabilityTest {
    CheckersBoard checkersBoard = new ThaiCheckersBoard();
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
    public void basicMoveTest() {
        Square[][] board = checkersBoard.getBoard();
        clearBoard(board);
        board[2][3].setPiece(Piece.Team.WHITE, Piece.PieceType.MAN);
        Assert.assertEquals(board[2][3].piece.checkLegalMoves(checkersBoard.getBackwardsLogic()).size(),2);
        board[2][3].setPiece(Piece.Team.WHITE, Piece.PieceType.KING);
        Assert.assertEquals(board[2][3].piece.checkLegalMoves(checkersBoard.getQueenLogic()).size(),11);
    }
    @Test
    public void jumpBackwardsTest() {
        Square[][] board = checkersBoard.getBoard();
        clearBoard(board);
        board[2][3].setPiece(Piece.Team.WHITE, Piece.PieceType.MAN);
        board[3][4].setPiece(Piece.Team.WHITE, Piece.PieceType.MAN);
        board[3][2].setPiece(Piece.Team.WHITE, Piece.PieceType.MAN);
        board[1][4].setPiece(Piece.Team.BLACK, Piece.PieceType.MAN);
        board[1][2].setPiece(Piece.Team.BLACK, Piece.PieceType.MAN);
        Assert.assertNull(board[2][3].piece.checkLegalMoves(checkersBoard.getBackwardsLogic()));
    }


}
