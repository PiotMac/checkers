package net;
import org.example.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
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
    Button[][] buttons;
    GridLayout grid;
    public Piece.Team thisPlayerTeam;
    public Piece.Team anotherPlayerTeam;
    public Piece.PieceType attemptedMovedPieceType;
    public boolean successiveCaptureMode = false;
    public int successiveX = -1;
    public int successiveY = -1;
    public final List<int[]> successiveJumpedXYs = new ArrayList<>();
    private Dimension checkersBoardSize;
    Font font = new Font("Arial", Font.PLAIN, 20);
    private int player;
    public final static int PLAYER1 = 1;
    public final static int PLAYER2 = 2;
    String str;
    public CheckersClient() {}

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

    private void choose(String choice) {
        switch (choice) {
            case "1" -> frame.checkersBoard = new ThaiCheckersBoard();
            case "2" -> frame.checkersBoard = new EnglishCheckersBoard();
            case "3" -> frame.checkersBoard = new ShashkiBoard();
            default -> throw new IllegalArgumentException();
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
            System.out.println("Choose type of checkers that you want to play!: ");
            Scanner scanner = new Scanner(System.in);
            System.out.println("1 - Thai, 2 - English, 3 - Shashki\n");
            String choice = scanner.nextLine();
            frame.choose(choice);
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
            frame.choose(type);
            frame.setClient();
        }
    }

    public void setClient() {
        if (frame.checkersBoard instanceof EnglishCheckersBoard || frame.checkersBoard instanceof ThaiCheckersBoard) {
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
        } else if (frame.checkersBoard instanceof ShashkiBoard) {
            if (frame.player == PLAYER1) {
                frame.thisPlayerTeam = Piece.Team.WHITE;
                frame.anotherPlayerTeam = Piece.Team.BLACK;
                super.setTitle("Your turn!");
            }
            else if (frame.player == PLAYER2) {
                frame.thisPlayerTeam = Piece.Team.BLACK;
                frame.anotherPlayerTeam = Piece.Team.WHITE;
                super.setTitle("Opponent's turn!");
            }
        }
        frame.size = frame.checkersBoard.getSize();
        frame.checkersBoardSize = new Dimension(50 * size, 50 * size);
        frame.buttons = new Button[size][size];
        //frame.board = checkersBoard.getBoard();
        frame.checkersBoard.getBoard();
        frame.first_click = new int[]{-1, -1};
        frame.second_click = new int[]{-1, -1};
        frame.grid = new GridLayout(size, size);
        super.setLayout(frame.grid);
        super.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        createBoard();
    }

    public void createBoard() {
        if (buttons[0][0] != null) {
            for (Button[] buttonRow : buttons) {
                for (Button button : buttonRow) {
                    super.remove(button);
                }
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if ((i+j)%2==0) {
                    buttons[i][j] = new Button("");
                    buttons[i][j].setBackground(Color.BLACK);
                } else if (!(CheckersBoard.board[i][j].isTaken())) {
                    buttons[i][j] = new Button("");
                    buttons[i][j].setBackground(Color.RED);
                } else if (CheckersBoard.board[i][j].isTaken() && CheckersBoard.board[i][j].getTeam() == Piece.Team.BLACK) {
                    buttons[i][j] = new Button("O");
                    //buttons[count] = new Button("\u26C2");
                    buttons[i][j].setForeground(Color.BLACK);
                    buttons[i][j].setBackground(Color.RED);
                } else if (CheckersBoard.board[i][j].isTaken() && CheckersBoard.board[i][j].getTeam() == Piece.Team.WHITE) {
                    buttons[i][j] = new Button("O");
                    //buttons[count] = new Button("\u26C0");
                    buttons[i][j].setForeground(Color.WHITE);
                    buttons[i][j].setBackground(Color.RED);
                }
                buttons[i][j].setSize(300, 300);
                buttons[i][j].setFont(font);
                buttons[i][j].addActionListener(this);
                buttons[i][j].setActionCommand("" + i + " " + j);
                super.add(buttons[i][j]);
            }
        }
        super.setMinimumSize(checkersBoardSize);
        getBoardReady();
    }
    private void getBoardReady() {
        super.pack();
        super.setVisible(true);
        if (super.getTitle().equals("Opponent's turn!")) {
            for (Button[] buttonRow : buttons) {
                for (Button button : buttonRow) {
                    button.setEnabled(false);
                }
            }
            receive();
        }
        if (super.getTitle().equals("Your turn!")) {
            for (Button[] buttonRow : buttons) {
                for (Button button : buttonRow) {
                    button.setEnabled(true);
                }

            }
        }
    }

    public void reprintBoard(List<int[]> xys) {
        for (int[] xy : xys) {
            if (!(CheckersBoard.board[xy[0]][xy[1]]).isTaken()) {
                buttons[xy[0]][xy[1]].setLabel("");
            } else {
                if (CheckersBoard.board[xy[0]][xy[1]].isTaken() && CheckersBoard.board[xy[0]][xy[1]].getTeam() == Piece.Team.BLACK) {
                    //buttons[xy[0] * size + xy[1]].setLabel("\u26C2");
                    buttons[xy[0]][xy[1]].setForeground(Color.BLACK);
                } else if (CheckersBoard.board[xy[0]][xy[1]].isTaken() && CheckersBoard.board[xy[0]][xy[1]].getTeam() == Piece.Team.WHITE) {
                    //buttons[xy[0] * size + xy[1]].setLabel("\u26C0");
                    buttons[xy[0]][xy[1]].setForeground(Color.WHITE);
                }
                if (CheckersBoard.board[xy[0]][xy[1]].isTaken() && CheckersBoard.board[xy[0]][xy[1]].piece.getPieceType()== Piece.PieceType.MAN) {
                    buttons[xy[0]][xy[1]].setLabel("O");
                } else if (CheckersBoard.board[xy[0]][xy[1]].isTaken() && CheckersBoard.board[xy[0]][xy[1]].piece.getPieceType()== Piece.PieceType.KING) {
                    buttons[xy[0]][xy[1]].setLabel("Q");
                }
            }
        }
        getBoardReady();
    }
    private void send(int[] move) {
        //Wysylanie do serwera
        frame.out.println(move[0] + " " + move[1] + " " + move[2] + " " + move[3] + " " + move[4]+ " " + move[5]);
    }
    private void updateMove(int firstX, int firstY, int secondX, int secondY, boolean yourMove, boolean successiveCapMode) {
        List<int[]> squaresToUpdate = frame.checkersBoard.updateMove(firstX, firstY, secondX, secondY, yourMove, successiveCapMode);
        reprintBoard(squaresToUpdate);
    }
    private void receive() {
        try {
            //Odbieranie z serwera
            str = frame.in.readLine();
            if (str == null) {
                System.out.println("YOU'VE WON!");
                System.exit(0);
            }
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
            legalMovesBoard = frame.checkersBoard.checkForLegalMovesOnBoard(frame.thisPlayerTeam);
        } else {
            Piece.PieceType pieceType = CheckersBoard.board[successiveX][successiveY].piece.getPieceType();
            boolean functionality;
            if (pieceType == Piece.PieceType.KING) {
                functionality = checkersBoard.getQueenLogic();
            } else {
                functionality = checkersBoard.getBackwardsLogic();
            }
            List<int[]> singleSquareMoves = CheckersBoard.board[successiveX][successiveY].piece.checkLegalMoves(functionality);
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
            if (CheckersBoard.board[frame.first_click[0]][frame.first_click[1]] == null || !CheckersBoard.board[frame.first_click[0]][frame.first_click[1]].isTaken() || CheckersBoard.board[frame.first_click[0]][frame.first_click[1]].getTeam() != frame.thisPlayerTeam) {
                System.out.println("This square has no piece on it or is not your piece!");
                frame.first_click[0] = -1;
                frame.first_click[1] = -1;
            }
            if (legalMovesBoard.isEmpty()) {
                System.out.println("GAME OVER, YOU LOST");
                frame.out.println("END");
                System.exit(0);
            }
        }
        //Getting coordinates of the second click
        else if (frame.second_click[0] == -1) {
            attemptedMovedPieceType = CheckersBoard.board[first_click[0]][first_click[1]].piece.getPieceType();
            frame.second_click[0] = Integer.parseInt(coordinatesString[0]);
            frame.second_click[1] = Integer.parseInt(coordinatesString[1]);
            int[] coordinates = {frame.first_click[0], frame.first_click[1], frame.second_click[0], frame.second_click[1]};
            int[] attemptedMove = frame.checkersBoard.checkForMoves(legalMovesBoard, coordinates);
            if(CheckersBoard.board[frame.second_click[0]][frame.second_click[1]] == null || attemptedMove==null) {
                System.out.println("Not a legal move!");
            }
            else {
                if (attemptedMove[4]==1) {
                    frame.checkersBoard.isSuccessiveCaptureAvailable(attemptedMove);
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
