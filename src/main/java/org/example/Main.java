package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
/*
public class Main implements Runnable {
    private static int type;
    static CheckersBoardBuilder checkersBoardBuilder;
    public static void main(String[] args) {
        CheckersBoard checkersBoard = new CheckersBoard();
        checkersBoard.createBoard();
    }

    private Socket firstPlayer;
    private Socket secondPlayer;


    private final static int FIRST=1;
    private final static int SECOND=2;
    private static int turn=FIRST;


    public Game(Socket firstPlayer, Socket secondPlayer){
        this.firstPlayer = firstPlayer;
        this.secondPlayer= secondPlayer;


    }
    @Override
    public void run() {

        try{
            //Inicjalizacja pobieranie od socketa dla player1
            InputStream inputF = firstPlayer.getInputStream();
            BufferedReader inF = new BufferedReader(new InputStreamReader(inputF));

            //Inicjalizacja pobieranie od socketa dla player2
            InputStream inputS = secondPlayer.getInputStream();
            BufferedReader inS = new BufferedReader(new InputStreamReader(inputS));

            //Inicjalizacja Wysylania do socketa dla player1
            OutputStream outputF = firstPlayer.getOutputStream();
            PrintWriter outF = new PrintWriter(outputF, true);

            //Inicjalizacja Wysylania do socketa dla player2
            OutputStream outputS = secondPlayer.getOutputStream();
            PrintWriter outS = new PrintWriter(outputS, true);

            outF.println("1");
            outS.println("2");

            String line;
            do {
                if (turn==SECOND) {
                    // Odbieranie od socketa
                    line = inS.readLine();
                    // Wypisywanie na serwerze
                    System.out.println(line);
                    // Wysylanie do socketa
                    outF.println("-> (" + line + ")");
                    turn=FIRST;
                }

                if (turn==FIRST) {
                    // Odbieranie od socketa
                    line = inF.readLine();
                    // Wypisywanie na serwerze
                    System.out.println(line);
                    // Wysylanie do socketa
                    outS.println("-> (" + line + ")");
                    turn=SECOND;
                }
            } while (true);

        } catch (IOException ex) {
            System.err.println("ex");
        }
    }

    private void sendMove(DataOutputStream out, String text) throws IOException {
        out.writeChars(text);

    }

    public static void main(String[] args)
    {
        try (ServerSocket serverSocket = new ServerSocket(4444))
        {
            System.out.println("Server is working...")
            //Tworzenie socketa oraz innych narzędzi do komunikacji z serwerem
            Socket socket = new Socket("localhost", 4444);
            // Wysylanie do serwera
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            // Odbieranie z serwera
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Console console = System.console();
            String text = "";
            //Wybieranie typu drzewa
            text = console.readLine("1 - Polish, 2 - Brazilian, 3 - Canadian\n");
            try {
                type = Integer.parseInt(text);
            }
            catch (NumberFormatException ex) {
                throw new NumberFormatException();
            }
            switch (type) {
                case 1:
                    checkersBoardBuilder = new CheckersBoard();
                    break;
                case 2:
                    //checkersBoardBuilder = new CheckersBoardBrazilian();
                    break;
                case 3:
                    //checkersBoardBuilder = new CheckersBoardCanadian();
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
        catch (UnknownHostException ex)
        {
            System.out.println("Server not found: " + ex.getMessage());

        }
        catch (IOException ex)
        {
            System.out.println("I/O error: " + ex.getMessage());
        }




        System.out.println("Which type of checkers do you wish to play?");
        System.out.println("1 - Polish, 2 - Brazilian, 3 - Canadian");
        try
        {
            type = Integer.parseInt(args[0]);
        }
        catch(IllegalArgumentException e)
        {
            System.out.println("Wrong input!");
            System.exit(0);
        }
        switch (type) {
            case 1:
                checkersBoardBuilder = new CheckersBoard();
                break;
            case 2:
                //checkersBoardBuilder = new CheckersBoardBrazilian();
                break;
            case 3:
                //checkersBoardBuilder = new CheckersBoardCanadian();
                break;
            default:
                throw new IllegalArgumentException();
        }
        launch(args);
    }
}

 */