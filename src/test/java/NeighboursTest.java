import org.example.CheckersBoard;
import org.example.Square;
import org.junit.Assert;
import org.junit.Test;
public class NeighboursTest {
    CheckersBoard checkersBoard = new CheckersBoard();
    Square[] neighbours;
    @Test
    public void getNeighboursTest() {
        checkersBoard.createBoard();
        //Square from which we get neighbours
        neighbours = CheckersBoard.board[1][2].getNeighbours();
        //TopLeft
        Assert.assertEquals(CheckersBoard.board[0][1], neighbours[0]);
        //TopRight
        Assert.assertEquals(CheckersBoard.board[0][3], neighbours[1]);
        //BottomLeft
        Assert.assertEquals(CheckersBoard.board[2][1], neighbours[2]);
        //BottomRight
        Assert.assertEquals(CheckersBoard.board[2][3], neighbours[3]);
    }
}
