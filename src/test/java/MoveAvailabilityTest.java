import org.example.CheckersBoard;
import org.junit.Test;

public class MoveAvailabilityTest {
    CheckersBoard checkersBoard = new CheckersBoard(10);
    @Test
    public void basicMoveTest() {
        checkersBoard.getBoard();
        CheckersBoard.board[0][1].piece.checkLegalMoves();
        CheckersBoard.board[3][0].piece.checkLegalMoves();
        CheckersBoard.board[3][4].piece.checkLegalMoves();
        CheckersBoard.board[6][5].piece.checkLegalMoves();
        CheckersBoard.board[6][9].piece.checkLegalMoves();
    }
}
