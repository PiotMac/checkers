import org.example.CheckersBoard;

import org.junit.Assert;
import org.junit.Test;
public class NeighboursTest {
    CheckersBoard checkersBoard = new CheckersBoard();
    @Test
    public void getNeighboursTestBasic() {
        checkersBoard.createBoard();
        //TopLeft
        Assert.assertEquals(CheckersBoard.board[0][1], CheckersBoard.board[1][2].getTopLeftNeighbour());
        //TopRight
        Assert.assertEquals(CheckersBoard.board[2][1], CheckersBoard.board[1][2].getTopRightNeighbour());
        //BottomLeft
        Assert.assertEquals(CheckersBoard.board[0][3], CheckersBoard.board[1][2].getBottomLeftNeighbour());
        //BottomRight
        Assert.assertEquals(CheckersBoard.board[2][3], CheckersBoard.board[1][2].getBottomRightNeighbour());
    }
    @Test
    public void getNeighboursTestAdvanced() {
        checkersBoard.createBoard();
        //TopLeft
        Assert.assertEquals(CheckersBoard.board[8][1], CheckersBoard.board[9][2].getTopLeftNeighbour());
        //TopRight
        Assert.assertNull(CheckersBoard.board[9][2].getTopRightNeighbour());
        //BottomLeft
        Assert.assertEquals(CheckersBoard.board[8][3], CheckersBoard.board[9][2].getBottomLeftNeighbour());
        //BottomRight
        Assert.assertNull(CheckersBoard.board[9][2].getBottomRightNeighbour());
    }
}
