
import net.CheckersClient;
import org.example.CheckersBoard;
import org.example.Piece;
import org.example.Square;
import org.example.ThaiCheckersBoard;
import org.junit.Assert;
import org.junit.Test;


import java.util.Arrays;
import java.util.List;

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
        Assert.assertEquals(board[2][3].piece.checkLegalMoves(checkersBoard.getKingLogic()).size(),11);
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
    @Test
    public void farCaptureTest() {
        Square[][] board = checkersBoard.getBoard();
        clearBoard(board);
        board[2][3].setPiece(Piece.Team.BLACK, Piece.PieceType.MAN);
        board[5][6].setPiece(Piece.Team.WHITE, Piece.PieceType.KING);
        List<int[]> availableMove = board[5][6].piece.checkLegalMoves(checkersBoard.getKingLogic());
        Assert.assertEquals(availableMove.size(),1);
        Assert.assertArrayEquals(availableMove.get(0), new int[]{1,2,1,1});
    }
    @Test
    public void cornerMoveTest() {
        Square[][] board = checkersBoard.getBoard();
        clearBoard(board);
        board[1][6].setPiece(Piece.Team.WHITE, Piece.PieceType.MAN);
        board[0][7].setPiece(Piece.Team.WHITE, Piece.PieceType.KING);
        Assert.assertNull(board[0][7].piece.checkLegalMoves(checkersBoard.getKingLogic()));
    }


    @Test
    public void initialMoveAvailabilityTest() {
        Square[][] board = checkersBoard.getBoard();
        //clearBoard(board);
        List<int[]> initialLegalMoves = checkersBoard.checkForLegalMovesOnBoard(Piece.Team.BLACK);
        for (int[] move : initialLegalMoves) {
            System.out.println(Arrays.toString(move));
        }
    }

}
