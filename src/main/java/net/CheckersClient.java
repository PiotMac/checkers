package net;
import org.example.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class CheckersClient extends Frame implements ActionListener, Runnable {
    Label msg;
    Label output;
    Button send;
    Button polish;
    Button brazilian;
    Button canadian;
    static CheckersBoard checkersBoardBuilder;
    static CheckersClient frame;
    TextField input;
    Socket socket = null;
    PrintWriter out = null;
    BufferedReader in = null;
    int size;
    Square[][] board;
    public CheckersClient(CheckersBoard checkersBoardBuilder1) {
        checkersBoardBuilder = checkersBoardBuilder1;
        setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 40));
        msg = new Label("Checkers");
        size = checkersBoardBuilder.getSize();
        board = checkersBoardBuilder.getBoard();
        setLayout(new GridLayout(size, size));
        Label label;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == null) {
                    label = new Label("");
                    label.setAlignment(Label.CENTER);
                    label.setBackground(Color.BLACK);
                    add(label);
                } else if (!(board[i][j].isTaken())) {
                    label = new Label("");
                    label.setAlignment(Label.CENTER);
                    label.setBackground(Color.WHITE);
                    add(label);
                } else if (board[i][j].isTaken() && board[i][j].getTeam() == Piece.Team.BLACK) {
                    label = new Label("⚫");
                    label.setAlignment(Label.CENTER);
                    label.setBackground(Color.WHITE);
                    add(label);
                } else if (board[i][j].isTaken() && board[i][j].getTeam() == Piece.Team.WHITE) {
                    label = new Label("\u26AA");
                    label.setAlignment(Label.CENTER);
                    label.setBackground(Color.WHITE);
                    add(label);
                }
            }
            System.out.println();
        }

        //input = new TextField(20);
        //output = new Label();
        //output.setBackground(Color.white);
        //send = new Button("Send");
        //send.addActionListener(this);
        //setLayout(new GridLayout(4, 1));
        //add(msg);
        //add(input);
        //add(send);
        //add(output);
    }

    private int player;

    public final static int PLAYER1 = 1;
    public final static int PLAYER2 = 2;

    public final static int ACTIVE = 0;
    public final static int NONACTIVE = 1;
    private  static int actualPlayer = PLAYER1;

    private static int showing = ACTIVE;

    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == polish) {
            checkersBoardBuilder = new CheckersBoard(10);
            dispose();
            send();
        }
    }

    private void send(){
        // Wysylanie do serwera
        out.println(input.getText());
        msg.setText("OppositeTurn");
        send.setEnabled(false);
        input.setText("");
        input.requestFocus();
        showing = ACTIVE;
        actualPlayer = player;
    }

    private void receive(){
        try {
            // Odbieranie z serwera
            String str = in.readLine();
            output.setText(str);
            msg.setText("My turn");
            send.setEnabled(true);
            input.setText("");
            input.requestFocus();
        }
        catch (IOException e) {
            System.out.println("Read failed"); System.exit(1);}
    }

    /*
    Połaczenie z socketem
     */
    public void listenSocket() {
        try {
            socket = new Socket("localhost", 4445);
            // Inicjalizacja wysylania do serwera
            out = new PrintWriter(socket.getOutputStream(), true);
            // Inicjalizacja odbierania z serwera
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (UnknownHostException e) {
            System.out.println("Unknown host: localhost");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("No I/O");
            System.exit(1);
        }
    }

    /*
        Poczatkowe ustawienia klienta. Ustalenie ktory socket jest ktorym kliente
    */
    private void receiveInitFromServer() {
        try {
            player = Integer.parseInt(in.readLine());
            if (player== PLAYER1) {
                msg.setText("My Turn");
            } else {
                msg.setText("Opposite turn");
                send.setEnabled(false);
            }
        } catch (IOException e) {
            System.out.println("Read failed");
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        System.out.println("Choose type of checkers that u want to play!: ");
        Scanner scanner = new Scanner(System.in);
        System.out.println("1 - Polish, 2 - Brazilian, 3 - Canadian\n");
        String text = scanner.nextLine();
        switch(text) {
            case "1":
                frame = new CheckersClient(new CheckersBoard(10));
                break;
            case "2":
                frame = new CheckersClient(new CheckersBoard(8));
                break;
            case "3":
                frame = new CheckersClient(new CheckersBoard(12));
                break;
            default:
                throw new IllegalArgumentException();
        }
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        frame.pack();
        frame.setVisible(true);
        frame.listenSocket();
        frame.receiveInitFromServer();
        frame.startThread();
    }

    private void startThread() {
        Thread checkersThread = new Thread(this);
        checkersThread.start();
    }

    @Override
    public void run() {
        if (player==PLAYER1) {
            f1();
        }
        else{
            f2();
        }
        // Mozna zrobic w jednej metodzie. Zostawiam
        // dla potrzeb prezentacji
        // f(player);
    }


    // Jedna metoda dla kazdego Playera
    void f(int iPlayer){
        while(true) {
            synchronized (this) {
                if (actualPlayer== iPlayer) {
                    try {
                        wait(10);
                    } catch (InterruptedException e) {
                    }
                }
                if (showing ==ACTIVE){
                    receive();
                    showing =NONACTIVE;
                }
                notifyAll();
            }
        }
    }

    /// Metoda uruchamiana w run dla PLAYER1
    void f1(){
        while(true) {
            synchronized (this) {
                if (actualPlayer== PLAYER1) {
                    try {
                        wait(10);
                    } catch (InterruptedException e) {
                    }
                }
                if (showing ==ACTIVE){
                    receive();
                    showing =NONACTIVE;
                }
                notifyAll();
            }
        }
    }

    /// Metoda uruchamiana w run dla PLAYER2
    void f2(){
        while(true) {
            synchronized (this) {
                if (actualPlayer== PLAYER2) {
                    try {
                        wait(10);
                    } catch (InterruptedException e) {
                    }
                }
                if (showing ==ACTIVE){
                    receive();
                    showing =NONACTIVE;
                }
                notifyAll();
            }
        }
    }
}
