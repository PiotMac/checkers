package net;

import org.example.CheckersBoard;
import org.example.Square;

import java.io.*;
import java.net.*;

public class CheckersServerThread
{
    static CheckersClient client;
    static CheckersBoard checkersBoard;
    static Square[][] board;
    public static void main(String[] args)
    {
        try (ServerSocket serverSocket = new ServerSocket(4445)) {

            System.out.println("Server is listening on port 4445");

            while (true) {
                Socket firstClient = serverSocket.accept();
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
                    case "1" -> checkersBoard = new CheckersBoard(10);
                    case "2" -> checkersBoard = new CheckersBoard(8);
                    case "3" -> checkersBoard = new CheckersBoard(12);
                    default -> throw new IllegalArgumentException();
                }
                board = checkersBoard.getBoard();


                System.out.println("Waiting for the second player");

                Socket secondClient = serverSocket.accept();
                System.out.println("Second client connected");

                //Inicjalizacja pobieranie od socketa dla player2
                InputStream inputS = secondClient.getInputStream();
                BufferedReader inS = new BufferedReader(new InputStreamReader(inputS));
                //Inicjalizacja Wysylania do socketa dla player2
                OutputStream outputS = secondClient.getOutputStream();
                PrintWriter outS = new PrintWriter(outputS, true);
                outS.println("2");
                outS.println(choice);

                System.out.println("Starting the game . . .");
                Checkers checkers = new Checkers(firstClient, secondClient);
                Thread checkersThread = new Thread(checkers);
                checkersThread.start();

                // TO DO: Musi byc dokldnie dwoch klientow

            }

        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}