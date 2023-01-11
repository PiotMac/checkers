package net;
import org.example.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.reflect.Array;
import java.net.*;
import java.util.*;
import java.util.List;

public class CheckersClient extends Frame implements  ActionListener {
    int []first_click;
    int []second_click;
    CheckersBoard checkersBoard;
    public static CheckersClient frame;
    Socket socket = null;
    PrintWriter out = null;
    BufferedReader in = null;
    int size;
    Square[][] board;
    Button[] buttons;
    GridLayout grid;
    private Piece.Team thisPlayerTeam;
    private Piece.Team anotherPlayerTeam;
    private Piece.PieceType attemptedMovedPieceType;
    private boolean successiveCaptureMode = false;
    private int successiveX = -1;
    private int successiveY = -1;
    private final List<int[]> successiveJumpedXYs = new ArrayList<>();
    private Dimension checkersBoardSize;
    Font font = new Font("Arial", Font.PLAIN, 20);

    public CheckersClient() {}
    public void setClient() {
        if (frame.player == PLAYER1) {
            frame.thisPlayerTeam = Piece.Team.BLACK;
            frame.anotherPlayerTeam = Piece.Team.WHITE;
            super.setTitle("Your turn!");
        }
        else if (frame.player == PLAYER2) {
            frame.thisPlayerTeam = Piece.Team.WHITE;
            frame.anotherPlayerTeam = Piece.Team.BLACK;
            super.setTitle("Opponent's turn!");
        }
        //msg = new Label("Checkers");
        frame.size = frame.checkersBoard.getSize();
        frame.checkersBoardSize = new Dimension(50 * size, 50 * size);
        frame.buttons = new Button[size * size];
        frame.board = checkersBoard.getBoard();
        frame.first_click = new int[]{-1, -1};
        frame.second_click = new int[]{-1, -1};
        frame.grid = new GridLayout(size, size);
        super.setLayout(frame.grid);
        super.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        //startThread();
        createBoard();
    }

    public void createBoard() {
        int count = 0;
        if (buttons[0] != null) {
            for (Button button : buttons) {
                super.remove(button);
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if ((i+j)%2==0) {
                    buttons[count] = new Button("");
                    buttons[count].setBackground(Color.BLACK);
                } else if (!(board[i][j].isTaken())) {
                    buttons[count] = new Button("");
                    buttons[count].setBackground(Color.RED);
                } else if (board[i][j].isTaken() && board[i][j].getTeam() == Piece.Team.BLACK) {
                    buttons[count] = new Button("O");
                    //buttons[count] = new Button("\u26C2");
                    buttons[count].setForeground(Color.BLACK);
                    buttons[count].setBackground(Color.RED);
                } else if (board[i][j].isTaken() && board[i][j].getTeam() == Piece.Team.WHITE) {
                    buttons[count] = new Button("O");
                    //buttons[count] = new Button("\u26C0");
                    buttons[count].setForeground(Color.WHITE);
                    buttons[count].setBackground(Color.RED);
                }
                buttons[count].setSize(300, 300);
                buttons[count].setFont(font);
                buttons[count].addActionListener(this);
                buttons[count].setActionCommand("" + i + " " + j);
                super.add(buttons[count]);
                count++;
            }
        }
        super.setMinimumSize(checkersBoardSize);
        super.pack();
        super.setVisible(true);
        if (super.getTitle().equals("Opponent's turn!")) {
            for (Button button : buttons) {
                button.setEnabled(false);
            }
            receive();
        }
        if (super.getTitle().equals("Your turn!")) {
            for (Button button : buttons) {
                button.setEnabled(true);
            }
        }
    }

    public void reprintBoard(List<int[]> xys) {
        for (int[] xy : xys) {
            if (!(board[xy[0]][xy[1]]).isTaken()) {
                buttons[xy[0] * size + xy[1]].setLabel("");
            } else {
                if (board[xy[0]][xy[1]].isTaken() && board[xy[0]][xy[1]].getTeam() == Piece.Team.BLACK) {
                    //buttons[xy[0] * size + xy[1]].setLabel("\u26C2");
                    buttons[xy[0] * size + xy[1]].setForeground(Color.BLACK);
                } else if (board[xy[0]][xy[1]].isTaken() && board[xy[0]][xy[1]].getTeam() == Piece.Team.WHITE) {
                    //buttons[xy[0] * size + xy[1]].setLabel("\u26C0");
                    buttons[xy[0] * size + xy[1]].setForeground(Color.WHITE);
                }
                if (board[xy[0]][xy[1]].isTaken() && board[xy[0]][xy[1]].piece.getPieceType()== Piece.PieceType.MAN) {
                    buttons[xy[0] * size + xy[1]].setLabel("O");
                } else if (board[xy[0]][xy[1]].isTaken() && board[xy[0]][xy[1]].piece.getPieceType()== Piece.PieceType.KING) {
                    buttons[xy[0] * size + xy[1]].setLabel("Q");
                }
            }
        }
        super.pack();
        super.setVisible(true);
        if (super.getTitle().equals("Opponent's turn!")) {
            for (Button button : buttons) {
                button.setEnabled(false);
            }
            receive();
        }
        if (super.getTitle().equals("Your turn!")) {
            for (Button button : buttons) {
                button.setEnabled(true);
            }
        }
    }


    private int player;
    public final static int PLAYER1 = 1;
    public final static int PLAYER2 = 2;
    String str;

    private void send(int[] move) {
        //Wysylanie do serwera
        frame.out.println(move[0] + " " + move[1] + " " + move[2] + " " + move[3] + " " + move[4]+ " " + move[5]);
    }

    private int[] getJumpedPieceCoordinates (int firstClickX, int firstClickY, int secondClickX, int secondClickY) {
        final int xDelta = secondClickX-firstClickX;
        final int yDelta = secondClickY-firstClickY;
        if (Math.abs(xDelta-yDelta)!=1) {
            if (xDelta>0) {
                if (yDelta>0) {
                    return new int[]{secondClickX-1, secondClickY-1};
                } else {
                    return new int[]{secondClickX-1, secondClickY+1};
                }
            } else {
                if (yDelta>0) {
                    return new int[]{secondClickX+1, secondClickY-1};
                } else {
                    return new int[]{secondClickX+1, secondClickY+1};
                }
            }
        }
        return null;
    }

    private void updateMove(int firstX, int firstY, int secondX, int secondY, boolean yourMove, boolean successiveCapMode) {
        List<int[]> squaresToUpdate = new ArrayList<>();
        board[firstX][firstY].setTaken(false);
        squaresToUpdate.add(new int[] {firstX, firstY});
        squaresToUpdate.add(new int[] {secondX, secondY});
        boolean addedKing = false;

        int[] potentiallyJumped = getJumpedPieceCoordinates(firstX, firstY, secondX, secondY);
        if (potentiallyJumped!=null) {
            board[potentiallyJumped[0]][potentiallyJumped[1]].setTaken(false);
            squaresToUpdate.add(potentiallyJumped);
        }

        if (secondX==size-1 ) {
            if (thisPlayerTeam == Piece.Team.WHITE && yourMove) {
                board[secondX][secondY].setPiece(thisPlayerTeam, Piece.PieceType.KING);
            } else if (thisPlayerTeam == Piece.Team.BLACK && !yourMove) {
                board[secondX][secondY].setPiece(anotherPlayerTeam, Piece.PieceType.KING);
            }
            clearSuccessive();
            successiveCapMode=false;
            addedKing = true;
        }
        if (secondX==0) {
            if (thisPlayerTeam == Piece.Team.BLACK && yourMove) {
                board[secondX][secondY].setPiece(thisPlayerTeam, Piece.PieceType.KING);
            } else if (thisPlayerTeam == Piece.Team.WHITE && !yourMove) {
                board[secondX][secondY].setPiece(anotherPlayerTeam, Piece.PieceType.KING);
            }
            clearSuccessive();
            successiveCapMode=false;
            addedKing = true;
        }
        if(!successiveCapMode) {
            if (yourMove) {
                if (!addedKing) {
                    board[secondX][secondY].setPiece(thisPlayerTeam, attemptedMovedPieceType);
                }
                super.setTitle("Opponent's turn!");
                System.out.println("Successful move!");
            } else {
                if (!addedKing) {
                    board[secondX][secondY].setPiece(anotherPlayerTeam, attemptedMovedPieceType);
                }
                super.setTitle("Your turn!");
                System.out.println("Opponent made a move");
            }

        } else {
            if (yourMove) {
                board[secondX][secondY].setPiece(thisPlayerTeam, attemptedMovedPieceType);
                System.out.println("Successive capture possible!");
            } else {
                board[secondX][secondY].setPiece(anotherPlayerTeam, attemptedMovedPieceType);
                System.out.println("You fell victim to a successive capture");
            }

        }
        reprintBoard(squaresToUpdate);
    }


    private void receive() {
        try {
            //Odbieranie z serwera
            str = frame.in.readLine();
            String[] coordinates = str.split(" ");
            //If this player made a move
            if(coordinates.length==1 && Integer.parseInt(coordinates[0])==0) {
                updateMove(frame.first_click[0], frame.first_click[1], frame.second_click[0], frame.second_click[1], true, successiveCaptureMode);
            } else {
                int[] coords = new int[6];
                for (int i = 0; i<coordinates.length; i++) {
                    coords[i] = Integer.parseInt(coordinates[i]);
                }
                if (coords[5]==0) {
                    attemptedMovedPieceType = Piece.PieceType.MAN;
                } else if (coords[5]==1) {
                    attemptedMovedPieceType = Piece.PieceType.KING;
                }
                updateMove(coords[0],coords[1],coords[2],coords[3], false, coords[4]==2);
            }

        } catch (IOException e) {
            System.out.println("Read failed");
            System.exit(1);
        }
    }

    /*
    Połaczenie z socketem
     */
    public void listenSocket() {
        try {
            frame.socket = new Socket("localhost", 4445);
            // Inicjalizacja wysylania do serwera
            frame.out = new PrintWriter(socket.getOutputStream(), true);
            // Inicjalizacja odbierania z serwera
            frame.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (UnknownHostException e) {
            System.out.println("Unknown host: localhost");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("No I/O");
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        frame = new CheckersClient();
        frame.listenSocket();
        try {
            frame.player = Integer.parseInt(frame.in.readLine());
        }
        catch (IOException e) {
            System.out.println("Read failed");
            System.exit(1);
        }
        catch (NumberFormatException e) {
            System.out.println("Limit of players has been reached!");
            System.exit(1);
        }
        if (frame.player == PLAYER1) {
            System.out.println("Choose type of checkers that u want to play!: ");
            Scanner scanner = new Scanner(System.in);
            System.out.println("1 - Polish, 2 - Brazilian, 3 - Canadian\n");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> frame.checkersBoard = new CheckersBoard(10);
                case "2" -> frame.checkersBoard = new CheckersBoard(8);
                case "3" -> frame.checkersBoard = new CheckersBoard(12);
                default -> throw new IllegalArgumentException();
            }
            frame.out.println(choice);
            frame.setClient();
        }
        else {
            String type = "";
            try {
                type = frame.in.readLine();
            } catch (IOException e) {
                System.out.println("Read failed");
                System.exit(1);
            }

            switch (type) {
                case "1" -> frame.checkersBoard = new CheckersBoard(10);
                case "2" -> frame.checkersBoard = new CheckersBoard(8);
                case "3" -> frame.checkersBoard = new CheckersBoard(12);
                default -> throw new IllegalArgumentException();
            }
            frame.setClient();
        }
    }
    public List<int[]> checkForLegalMovesOnBoard() {
        List<int[]> notCaptureList = new ArrayList<>();
        List<int[]> captureList = new ArrayList<>();
        boolean captureAvailable = false;
        for (int i = 0; i<frame.board.length; i++) {
            for (int j = 0; j<frame.board.length; j++) {
                if ((i+j)%2!=0 && frame.board[i][j].piece!=null && frame.board[i][j].piece.checkLegalMoves()!=null && frame.board[i][j].isTaken() && frame.board[i][j].getTeam().equals(thisPlayerTeam)) {
                    for (int[] move : frame.board[i][j].piece.checkLegalMoves()) {
                        if (move[2]>0) {
                            captureList.add(new int[]{i,j,move[0],move[1],move[2],move[3]});
                            captureAvailable = true;
                            notCaptureList.clear();
                        }
                        else if (!captureAvailable) {
                            notCaptureList.add(new int[]{i,j,move[0],move[1],move[2],move[3]});
                        }
                    }
                }
            }
        }
        if (captureAvailable) {
            return captureList;
        } else {
            return notCaptureList;
        }
    }
    private void clearSuccessive() {
        successiveCaptureMode = false;
        successiveX=-1;
        successiveY=-1;
        successiveJumpedXYs.clear();
    }
    private void isSuccessiveCaptureAvailable(int[] attemptedMove) {
        Square[][] boardClone = frame.board.clone();
        boolean goodMove = false;
        boardClone[attemptedMove[0]][attemptedMove[1]].setTaken(false);
        boardClone[attemptedMove[2]][attemptedMove[3]].setPiece(thisPlayerTeam, attemptedMovedPieceType);
        successiveJumpedXYs.add(getJumpedPieceCoordinates(attemptedMove[0],attemptedMove[1],attemptedMove[2],attemptedMove[3]));
        List<int[]> possibleNextMoves = boardClone[attemptedMove[2]][attemptedMove[3]].piece.checkLegalMoves();
        if (possibleNextMoves!=null) {
            for (int[] move : possibleNextMoves) {
                if (move[2]>0) {
                    goodMove=true;
                    for (int[] jumpedXY : successiveJumpedXYs) {
                        if (Arrays.equals(getJumpedPieceCoordinates(attemptedMove[2], attemptedMove[3], move[0], move[1]),jumpedXY)) {
                            goodMove=false;
                            break;
                        }
                    }
                    if (goodMove) {
                        successiveCaptureMode = true;
                        successiveX=attemptedMove[2];
                        successiveY=attemptedMove[3];
                        break;
                    }
                }
            }
        }
        if (!goodMove) {
            reprintBoard(successiveJumpedXYs);
            clearSuccessive();
        }
    }

    public int[] checkForMoves(List<int[]> moveList, int[] coordinates) {
        for (int[] ints : moveList) {
            if (ints[0] == coordinates[0] && ints[1] == coordinates[1] && ints[2] == coordinates[2] && ints[3] == coordinates[3] ) {
                return ints;
            }
        }
        return null;
    }
    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        String[] coordinatesString = command.split(" ");
        List<int[]> legalMovesBoard = new ArrayList<>();
        if (!successiveCaptureMode) {
            legalMovesBoard = checkForLegalMovesOnBoard();
        } else {
            List<int[]> singleSquareMoves = frame.board[successiveX][successiveY].piece.checkLegalMoves();
            for (int[] move : singleSquareMoves) {
                if(move[2]>0) {
                    legalMovesBoard.add(new int[]{successiveX, successiveY, move[0], move[1], move[2], move[3]});
                }
            }
        }

        //Getting coordinates of the first click
        if (frame.first_click[0] == -1) {
            frame.first_click[0] = Integer.parseInt(coordinatesString[0]);
            frame.first_click[1] = Integer.parseInt(coordinatesString[1]);
            if (frame.board[frame.first_click[0]][frame.first_click[1]] == null || !frame.board[frame.first_click[0]][frame.first_click[1]].isTaken() || frame.board[frame.first_click[0]][frame.first_click[1]].getTeam() != frame.thisPlayerTeam) {
                System.out.println("This square has no piece on it or is not your piece!");
                frame.first_click[0] = -1;
                frame.first_click[1] = -1;
            }
            if (legalMovesBoard.isEmpty()) {
                System.out.println("GAME OVER, YOU LOST");
            }
        }
        //Getting coordinates of the second click
        else if (frame.second_click[0] == -1) {
            attemptedMovedPieceType = frame.board[first_click[0]][first_click[1]].piece.getPieceType();
            frame.second_click[0] = Integer.parseInt(coordinatesString[0]);
            frame.second_click[1] = Integer.parseInt(coordinatesString[1]);
            int[] coordinates = {frame.first_click[0], frame.first_click[1], frame.second_click[0], frame.second_click[1]};
            int[] attemptedMove = checkForMoves(legalMovesBoard, coordinates);
            if(frame.board[frame.second_click[0]][frame.second_click[1]] == null || attemptedMove==null) {
                System.out.println("Not a legal move!");
            } else {
                if (attemptedMove[4]==1) {
                    isSuccessiveCaptureAvailable(attemptedMove);
                }
                if (successiveCaptureMode) {
                    attemptedMove[4]=2;
                }
                send(attemptedMove);
                receive();
            }
            for (int i = 0; i < 2; i++) {
                frame.first_click[i] = -1;
                frame.second_click[i] = -1;
            }
        }
    }
}
