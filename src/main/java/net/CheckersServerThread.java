package net;

import org.example.*;

import java.io.*;
import java.net.*;

public class CheckersServerThread
{
    static CheckersClient client;
    static CheckersBoard checkersBoard;
    static Square[][] board;
    public static boolean playingGame;
    static Thread checkersThread;
    static ServerSocket serverSocket;
    static Socket firstClient;
    static Socket secondClient;
    public static void main(String[] args)
    {
        try {
            serverSocket = new ServerSocket(4445);
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
        System.out.println("Server is listening on port 4445");
        init();
    }
    public static void end() throws IOException {
        checkersThread.interrupt();
        System.out.println("Closing both clients...");
        //Closing clients
        firstClient.close();
        secondClient.close();
        System.out.println("The game has ended! Waiting for new players...");
        init();
    }
    private static void init() {
        while (true) {
            try {
                firstClient = serverSocket.accept();
                System.out.println("First client connected");
                //Inicjalizacja pobieranie od socketa dla player1
                InputStream inputF = firstClient.getInputStream();
                BufferedReader inF = new BufferedReader(new InputStreamReader(inputF));
                //Inicjalizacja Wysylania do socketa dla player1
                OutputStream outputF = firstClient.getOutputStream();
                PrintWriter outF = new PrintWriter(outputF, true);
                outF.println("1");
                System.out.println("Creating board . . .");
                String choice = inF.readLine();
                client = new CheckersClient();
                switch (choice) {
                    case "1" -> checkersBoard = new ThaiCheckersBoard();
                    case "2" -> checkersBoard = new EnglishCheckersBoard();
                    case "3" -> checkersBoard = new ShashkiBoard();
                    default -> throw new IllegalArgumentException();
                }
                board = checkersBoard.getBoard();


                System.out.println("Waiting for the second player");

                secondClient = serverSocket.accept();
                System.out.println("Second client connected");

                //Inicjalizacja pobieranie od socketa dla player2
                InputStream inputS = secondClient.getInputStream();
                BufferedReader inS = new BufferedReader(new InputStreamReader(inputS));
                //Inicjalizacja Wysylania do socketa dla player2
                OutputStream outputS = secondClient.getOutputStream();
                PrintWriter outS = new PrintWriter(outputS, true);
                outS.println("2");
                outS.println(choice);

                playingGame = true;
                System.out.println("Starting the game . . .");
                Checkers checkers = new Checkers(firstClient, secondClient);
                checkersThread = new Thread(checkers);
                checkersThread.start();

                // TODO: Musi byc dokldnie dwoch klientow
                // Zatem po skończonej rozgrywce do serwera będzie trzeba
                // wysłać sygnał, że gra została zakończona

                while(playingGame) {
                    Socket waitingClient = serverSocket.accept();
                    OutputStream outputW = waitingClient.getOutputStream();
                    PrintWriter outW = new PrintWriter(outputW, true);
                    outW.println("Limit of players has been reached!");
                    System.out.println("Prevented new player from joining the server!");
                    waitingClient.close();
                }
            }
            catch (IOException ex) {
                System.out.println("Server exception: " + ex.getMessage());
                ex.printStackTrace();
            }
        }

    }
}