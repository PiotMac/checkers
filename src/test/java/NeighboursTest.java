import org.example.CheckersBoard;

import org.example.EnglishCheckersBoard;
import org.junit.Assert;
import org.junit.Test;
public class NeighboursTest {
    CheckersBoard checkersBoard = new EnglishCheckersBoard();
    @Test
    public void getNeighboursTestBasic() {
        checkersBoard.getBoard();
        //TopLeft
        Assert.assertEquals(CheckersBoard.board[0][1], CheckersBoard.board[1][2].getTopLeftNeighbour());
        //TopRight
        Assert.assertEquals(CheckersBoard.board[0][3], CheckersBoard.board[1][2].getTopRightNeighbour());
        //BottomLeft
        Assert.assertEquals(CheckersBoard.board[2][1], CheckersBoard.board[1][2].getBottomLeftNeighbour());
        //BottomRight
        Assert.assertEquals(CheckersBoard.board[2][3], CheckersBoard.board[1][2].getBottomRightNeighbour());
    }
    @Test
    public void getNeighboursTestAdvanced() {
        checkersBoard.getBoard();
        //TopLeft
        Assert.assertEquals(CheckersBoard.board[1][0], CheckersBoard.board[0][1].getBottomLeftNeighbour());
        //TopRight
        Assert.assertEquals(CheckersBoard.board[1][2], CheckersBoard.board[0][1].getBottomRightNeighbour());
        //BottomLeft
        Assert.assertNull(CheckersBoard.board[0][1].getTopLeftNeighbour());
        //BottomRight
        Assert.assertNull(CheckersBoard.board[0][1].getTopRightNeighbour());
    }
}
