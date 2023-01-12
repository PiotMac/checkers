
import org.example.CheckersBoard;
import org.example.Piece;
import org.example.Square;
import org.example.ThaiCheckersBoard;
import org.junit.Assert;
import org.junit.Test;



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
    public void doubleOpposingSquareKingTest() {
        Square[][] board = checkersBoard.getBoard();
        clearBoard(board);
        board[0][3].setPiece(Piece.Team.BLACK, Piece.PieceType.KING);
        board[1][2].setPiece(Piece.Team.WHITE, Piece.PieceType.MAN);
        board[2][1].setPiece(Piece.Team.WHITE, Piece.PieceType.MAN);
        Assert.assertEquals(board[0][3].piece.checkLegalMoves(checkersBoard.getKingLogic()).size(), 4);
    }

    @Test
    public void initialMoveAvailabilityTest() {
        checkersBoard.getBoard();
        //clearBoard(board);
        List<int[]> initialLegalMoves = checkersBoard.checkForLegalMovesOnBoard(Piece.Team.BLACK);
        Assert.assertEquals(initialLegalMoves.size(), 7);
        initialLegalMoves = checkersBoard.checkForLegalMovesOnBoard(Piece.Team.WHITE);
        Assert.assertEquals(initialLegalMoves.size(), 7);
    }

    @Test
    public void moveAvailabilityAdvancedTest() {
        Square[][] board = checkersBoard.getBoard();
        clearBoard(board);
        board[5][6].setPiece(Piece.Team.WHITE, Piece.PieceType.MAN);
        board[2][3].setPiece(Piece.Team.BLACK, Piece.PieceType.KING);
        board[6][1].setPiece(Piece.Team.BLACK, Piece.PieceType.MAN);
        board[7][0].setPiece(Piece.Team.WHITE, Piece.PieceType.MAN);
        List<int[]> legalMoves = checkersBoard.checkForLegalMovesOnBoard(Piece.Team.BLACK);
        Assert.assertEquals(legalMoves.size(), 1);
        Assert.assertArrayEquals(legalMoves.get(0), new int[]{2,3,6,7,1,1});
    }
    @Test
    public void successiveCaptureSim() {
        Square[][] board = checkersBoard.getBoard();
        clearBoard(board);
        board[3][4].setPiece(Piece.Team.WHITE, Piece.PieceType.MAN);
        board[4][3].setPiece(Piece.Team.BLACK, Piece.PieceType.MAN);
        board[6][3].setPiece(Piece.Team.BLACK, Piece.PieceType.MAN);
        List<int[]> legalMoves = checkersBoard.checkForLegalMovesOnBoard(Piece.Team.WHITE);
        //asserting there's only one legal move
        Assert.assertEquals(legalMoves.size(), 1);
        int[] move = legalMoves.get(0);
        board[move[0]][move[1]].setTaken(false);
        board[move[2]][move[3]].setPiece(Piece.Team.WHITE, Piece.PieceType.MAN);
        int[] jumpedCoordinates = checkersBoard.getJumpedPieceCoordinates(move[0], move[1], move[2], move[3]);
        board[jumpedCoordinates[0]][jumpedCoordinates[1]].setTaken(false);
        legalMoves = checkersBoard.checkForLegalMovesOnBoard(Piece.Team.WHITE);
        Assert.assertEquals(legalMoves.size(), 1);
        board[move[2]][move[3]].setPiece(Piece.Team.WHITE, Piece.PieceType.MAN);
        jumpedCoordinates = checkersBoard.getJumpedPieceCoordinates(move[0], move[1], move[2], move[3]);
        board[jumpedCoordinates[0]][jumpedCoordinates[1]].setTaken(false);
    }

}
