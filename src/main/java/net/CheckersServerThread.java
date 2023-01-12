package net;

import org.example.*;

import java.io.*;
import java.net.*;

/**
 * Klasa serwera
 */
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

    /**
     * Klasa uruchamiająca serwer
     * @param args - defaultowe argumenty maina
     */
    public static void main(String[] args) {
        // Włączenie serwera
        try {
            serverSocket = new ServerSocket(4445);
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
        System.out.println("Server is listening on port 4445");
        // Konfiguracja ustawień serwera
        init();
    }

    /**
     * Metoda odpowiedzialna za zakończenie rozgrywki po skończeniu gry
     * @throws IOException - wyjątek dbający o odpowiednie przerwanie wątku oraz odłączenie klientów
     */
    public static void end() throws IOException {
        // Przerwanie gry
        checkersThread.interrupt();
        System.out.println("Closing both clients...");
        // Odłączanie klientów
        firstClient.close();
        secondClient.close();
        System.out.println("The game has ended! Waiting for new players...");
        // Czekanie na kolejnych graczy
        init();
    }

    /**
     * Metoda odpowiedzialna za uruchomienie gry dla dwóch pierwszych klientów
     */
    private static void init() {
        while (true) {
            try {
                // Czekanie na pierwszego gracza
                firstClient = serverSocket.accept();
                System.out.println("First client connected");
                // Inicjalizacja pobierania od socketa dla player1
                InputStream inputF = firstClient.getInputStream();
                BufferedReader inF = new BufferedReader(new InputStreamReader(inputF));
                // Inicjalizacja wysyłania do socketa dla player1
                OutputStream outputF = firstClient.getOutputStream();
                PrintWriter outF = new PrintWriter(outputF, true);
                // Wysyłanie ID gracza
                outF.println("1");
                System.out.println("Creating board . . .");
                // Tworzenie planszy na podstawie wyboru pierwszego gracza
                String choice = inF.readLine();
                client = new CheckersClient();
                switch (choice) {
                    case "1" -> checkersBoard = new ThaiCheckersBoard();
                    case "2" -> checkersBoard = new EnglishCheckersBoard();
                    case "3" -> checkersBoard = new ShashkiCheckersBoard();
                    default -> throw new IllegalArgumentException();
                }
                board = checkersBoard.getBoard();

                System.out.println("Waiting for the second player");
                // Czekanie na drugiego gracza
                secondClient = serverSocket.accept();
                System.out.println("Second client connected");
                // Inicjalizacja pobierania od socketa dla player2
                InputStream inputS = secondClient.getInputStream();
                BufferedReader inS = new BufferedReader(new InputStreamReader(inputS));
                // Inicjalizacja wysyłania do socketa dla player2
                OutputStream outputS = secondClient.getOutputStream();
                PrintWriter outS = new PrintWriter(outputS, true);
                // Wysyłanie ID gracza
                outS.println("2");
                // Tworzenie planszy dla drugiego gracza
                outS.println(choice);

                playingGame = true;
                System.out.println("Starting the game . . .");
                // Rozpoczęcie rozgrywki
                Checkers checkers = new Checkers(firstClient, secondClient);
                checkersThread = new Thread(checkers);
                checkersThread.start();

                // Dopóki rozgrywana jest gra żaden nowy klient nie może dołączyć do serwera
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