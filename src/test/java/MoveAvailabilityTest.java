import org.example.CheckersBoard;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class MoveAvailabilityTest {
    CheckersBoard checkersBoard = new CheckersBoard(10);
    @Test
    public void basicMoveTest() {
        checkersBoard.getBoard();
        Assert.assertNull(CheckersBoard.board[0][1].piece.checkLegalMoves());
        for (int[] moves : CheckersBoard.board[3][4].piece.checkLegalMoves()) {
            System.out.println(Arrays.toString(moves));
        }
        //CheckersBoard.board[3][0].piece.checkLegalMoves();
        //CheckersBoard.board[3][4].piece.checkLegalMoves();
        //CheckersBoard.board[6][5].piece.checkLegalMoves();
        //CheckersBoard.board[6][9].piece.checkLegalMoves();
    }
    @Test
    public void boardAsArrayTest() {
        checkersBoard.getBoard();
        int[] brd = checkersBoard.getBoardAsArray();
        int[] prepared = new int[50];
        for (int i=0; i<50; i++) {
            if (i<20) {
                prepared[i] = -1;
            } else if (i<30) {
                prepared[i] = 0;
            } else {
                prepared[i] = 1;
            }
        }
        for (int i = 0; i<50; i++) {
            Assert.assertEquals(brd[i],prepared[i]);
        }
    }
}
