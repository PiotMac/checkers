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
    Square[][] board;
    Button[] buttons;
    GridLayout grid;
    private Piece.Team thisPlayerTeam;
    private Piece.Team anotherPlayerTeam;
    private boolean isCapturePossible = false;
    Font font = new Font("Arial", Font.PLAIN, 20);

    public CheckersClient() {}
    public void setClient() {
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
        //msg = new Label("Checkers");
        frame.size = frame.checkersBoard.getSize();
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
                    buttons[count].setForeground(Color.BLACK);
                    buttons[count].setBackground(Color.RED);
                } else if (board[i][j].isTaken() && board[i][j].getTeam() == Piece.Team.WHITE) {
                    buttons[count] = new Button("O");
                    buttons[count].setForeground(Color.WHITE);
                    buttons[count].setBackground(Color.RED);
                }
                buttons[count].setFont(font);
                buttons[count].addActionListener(this);
                buttons[count].setActionCommand("" + i + " " + j);
                super.add(buttons[count]);
                count++;
            }
            System.out.println();
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

    public void reprintBoard(List<Integer> xys) {
        int limit = (int)Math.sqrt(buttons.length);
        for (int i = 0; i<xys.size(); i++) {
            if (i%2==0) {
                if (!(board[xys.get(i)][xys.get(i+1)].isTaken())) {
                    buttons[xys.get(i)*limit+xys.get(i+1)].setLabel("");
                } else if (board[xys.get(i)][xys.get(i+1)].isTaken() && board[xys.get(i)][xys.get(i+1)].getTeam() == Piece.Team.BLACK) {
                    buttons[xys.get(i)*limit+xys.get(i+1)].setLabel("O");
                    buttons[xys.get(i)*limit+xys.get(i+1)].setForeground(Color.BLACK);
                } else if (board[xys.get(i)][xys.get(i+1)].isTaken() && board[xys.get(i)][xys.get(i+1)].getTeam() == Piece.Team.WHITE) {
                    buttons[xys.get(i)*limit+xys.get(i+1)].setLabel("O");
                    buttons[xys.get(i)*limit+xys.get(i+1)].setForeground(Color.WHITE);
                }
                buttons[xys.get(i)*limit+xys.get(i+1)].setBackground(Color.RED);
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

    private void send() {
        //Wysylanie do serwera
        if (isCapturePossible) {
            frame.out.println(first_click[0] + " " + first_click[1] + " " + second_click[0] + " " + second_click[1] + " 1");
        } else {
            frame.out.println(first_click[0] + " " + first_click[1] + " " + second_click[0] + " " + second_click[1] + " 0");
        }
    }

    private List<Integer> clearPieces(int firstClickX, int firstClickY, int secondClickX, int secondClickY) {
        final int xDelta = secondClickX-firstClickX;
        final int yDelta = secondClickY-firstClickY;
        List<Integer> xys = new ArrayList<>();
        for (int i = 0; i<=xDelta; i++) {
            if (xDelta > 0 && yDelta > 0) {
                frame.board[firstClickX+i][firstClickY+i].setTaken(false);
                xys.add(firstClickX+i);
                xys.add(firstClickY+i);
            }
            if (xDelta > 0 && yDelta < 0) {
                frame.board[firstClickX+i][firstClickY-i].setTaken(false);
                xys.add(firstClickX+i);
                xys.add(firstClickY-i);
            }
        }
        for (int i = 0; i>=xDelta; i--) {
            if (xDelta < 0 && yDelta > 0) {
                frame.board[firstClickX+i][firstClickY-i].setTaken(false);
                xys.add(firstClickX+i);
                xys.add(firstClickY-i);
            }
            if (xDelta < 0 && yDelta < 0) {
                frame.board[firstClickX+i][firstClickY+i].setTaken(false);
                xys.add(firstClickX+i);
                xys.add(firstClickY+i);
            }
        }
        return xys;
    }

    private void receive() {
        try {
            //Odbieranie z serwera
            str = frame.in.readLine();
            String[] coordinates = str.split(" ");
            //If this player made a move

            if (coordinates.length == 1 && Integer.parseInt(coordinates[0]) == 0) {
                List<Integer> xys = clearPieces(frame.first_click[0],frame.first_click[1],frame.second_click[0],frame.second_click[1]);
                frame.board[frame.second_click[0]][frame.second_click[1]].setPiece(thisPlayerTeam);
                super.setTitle("Opponent's turn!");
                reprintBoard(xys);
                System.out.println("Successful move!");
            }
            //If another player made a move
            else {
                List<Integer> xys = clearPieces(Integer.parseInt(coordinates[0]),Integer.parseInt(coordinates[1]),Integer.parseInt(coordinates[2]),Integer.parseInt(coordinates[3]));
                //frame.board[Integer.parseInt(coordinates[0])][Integer.parseInt(coordinates[1])].setTaken(false);
                frame.board[Integer.parseInt(coordinates[2])][Integer.parseInt(coordinates[3])].setPiece(anotherPlayerTeam);
                super.setTitle("Your turn!");
                reprintBoard(xys);
                System.out.println("Opponent made a move!");
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
        } catch (IOException e) {
            System.out.println("Read failed");
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
        List<int[]> masterList = new ArrayList<>();
        List<Integer> deltas = new ArrayList<>();
        int count = 0;
        for (int i = 0; i<frame.board.length; i++) {
            for (int j = 0; j<frame.board.length; j++) {
                if ((i+j)%2!=0 && frame.board[i][j].piece!=null && frame.board[i][j].piece.checkLegalMoves()!=null && frame.board[i][j].isTaken() && frame.board[i][j].getTeam().equals(thisPlayerTeam)) {
                    for (int[] move : frame.board[i][j].piece.checkLegalMoves()) {
                        int[] minorList = {i, j, move[0], move[1]};
                        masterList.add(minorList);
                        deltas.add(Math.abs(i-move[0]));
                        count = count+1;
                    }
                }
            }
        }

        List<int[]> returnList = new ArrayList<>();
        for (int k = 0; k<count; k++) {
            if (deltas.get(k).intValue() == Collections.max(deltas).intValue()) {
                returnList.add(masterList.get(k));
            }
        }
        if (Collections.max(deltas) >1) {
            System.out.println("Capture possible.");
            isCapturePossible = true;
        } else {
            isCapturePossible = false;
        }
        return returnList;
    }

    public boolean checkForMoves(List<int[]> moveList, int[] coordinates) {
        for (int[] ints : moveList) {
            if (ints[0] == coordinates[0] && ints[1] == coordinates[1] && ints[2] == coordinates[2] && ints[3] == coordinates[3] ) {
                return true;
            }
        }
        return false;
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
        //Getting coordinates of the first click
        if (frame.first_click[0] == -1) {
            frame.first_click[0] = Integer.parseInt(coordinatesString[0]);
            frame.first_click[1] = Integer.parseInt(coordinatesString[1]);
            if (frame.board[frame.first_click[0]][frame.first_click[1]] == null || !frame.board[frame.first_click[0]][frame.first_click[1]].isTaken() || frame.board[frame.first_click[0]][frame.first_click[1]].getTeam() != frame.thisPlayerTeam) {
                System.out.println("This square has no piece on it or is not your piece!");
                frame.first_click[0] = -1;
                frame.first_click[1] = -1;
            }
        }
        //Getting coordinates of the second click
        else if (frame.second_click[0] == -1) {
            List<int[]> legalMovesBoard = checkForLegalMovesOnBoard();
            frame.second_click[0] = Integer.parseInt(coordinatesString[0]);
            frame.second_click[1] = Integer.parseInt(coordinatesString[1]);
            int[] coordinates = {frame.first_click[0], frame.first_click[1], frame.second_click[0], frame.second_click[1]};

            for (int[] move : legalMovesBoard) {
                System.out.println(move[0]+" "+ move[1]+" "+ move[2]+" "+ move[3]);
            }
            if(frame.board[frame.second_click[0]][frame.second_click[1]] == null || !(checkForMoves(legalMovesBoard,coordinates))) {
                System.out.println("Not a legal move!");
            } else {
                send();
                receive();
            }
            for (int i = 0; i < 2; i++) {
                frame.first_click[i] = -1;
                frame.second_click[i] = -1;
            }
        }
    }
}
