import org.example.CheckersBoard;
import org.example.EnglishCheckersBoard;
import org.example.Piece;
import org.example.Square;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MoveAvailabilityTest {
    CheckersBoard checkersBoard = new EnglishCheckersBoard();
    @Test
    public void basicMoveTest() {
        Square[][] board = checkersBoard.getBoard();
        Assert.assertNull(board[0][1].piece.checkLegalMoves(checkersBoard.getBackwardsLogic()));//a blocked piece should not have legal moves
        List<int[]> legalMoves = board[2][3].piece.checkLegalMoves(checkersBoard.getBackwardsLogic());
        List<int[]> mockList = new ArrayList<>();
        mockList.add(new int[]{3,2,0,0});
        mockList.add(new int[]{3,4,0,0});
        int i = 0;
        for (int[] move : legalMoves) {
            Assert.assertArrayEquals(move, mockList.get(i));
            i=i+1;
        }
    }
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
    public void jumpTest() {
        Square[][] board = checkersBoard.getBoard();
        clearBoard(board);
        board[3][2].setPiece(Piece.Team.WHITE, Piece.PieceType.MAN);
        board[4][3].setPiece(Piece.Team.BLACK, Piece.PieceType.MAN);
        board[5][2].setPiece(Piece.Team.WHITE, Piece.PieceType.MAN);

        //you have to jump if that possibility exists, you can't jump backwards
        List<int[]> jump = board[4][3].piece.checkLegalMoves(checkersBoard.getBackwardsLogic());
        Assert.assertEquals(jump.size(),1);
        Assert.assertArrayEquals(jump.get(0), new int[]{2,1,1,0});

    }

    @Test
    public void KingTest() {
        Square[][] board = checkersBoard.getBoard();
        clearBoard(board);
        board[3][2].setPiece(Piece.Team.WHITE, Piece.PieceType.MAN);
        board[4][3].setPiece(Piece.Team.BLACK, Piece.PieceType.KING);
        board[5][2].setPiece(Piece.Team.WHITE, Piece.PieceType.MAN);

        //you can jump backwards as a king
        List<int[]> jump = board[4][3].piece.checkLegalMoves(checkersBoard.getKingLogic());
        Assert.assertEquals(jump.size(),2);
        Assert.assertArrayEquals(jump.get(0), new int[]{2,1,1,1});
        Assert.assertArrayEquals(jump.get(1), new int[]{6,1,1,1});
    }
    @Test
    public void typeTest() {
        Square[][] board = checkersBoard.getBoard();
        clearBoard(board);
        board[3][2].setPiece(Piece.Team.WHITE, Piece.PieceType.MAN);
        board[4][3].setPiece(Piece.Team.BLACK, Piece.PieceType.KING);
        Assert.assertEquals(board[3][2].piece.getPieceType(), Piece.PieceType.MAN);
        Assert.assertEquals(board[4][3].piece.getPieceType(), Piece.PieceType.KING);
    }
}
