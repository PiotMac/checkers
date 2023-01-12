package net;

import java.io.*;
import java.net.Socket;

/**
 * Klasa odpowiadająca za wątek rozgrywki pomiędzy dwoma graczami.
 */
public class Checkers implements Runnable {
    private final Socket firstPlayer;
    private final Socket secondPlayer;
    private final static int FIRST=1;
    private final static int SECOND=2;
    private static int turn=FIRST;
    private final int[] first_click = new int[2];
    private final int[] second_click = new int[2];

    /**
     * Konstruktor przyjmujący dwóch klientów
     * @param firstPlayer - instancja pierwszego gracza
     * @param secondPlayer - instancja drugiego gracza
     */
    public Checkers(Socket firstPlayer, Socket secondPlayer) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
    }

    /**
     * Metoda zmieniająca tury graczy po wykonaniu ruchu
     */
    private void switchTurn() {
        if (turn==SECOND) {
            turn = FIRST;
        } else {
            turn = SECOND;
        }
    }

    /**
     * Metoda nadzorująca aktualny stan gry
     * @param inR - input klienta
     * @param outLocal - output wysyłany do klienta, który zrobił ruch
     * @param outOpp - output wysyłany do klienta, który czekał na ruch przeciwnika
     * @throws IOException - wyjątek nadzorujący poprawne działanie wątku
     */
    private void gameState(BufferedReader inR, PrintWriter outLocal, PrintWriter outOpp) throws IOException {
        // Odbieranie od socketa
        String line = inR.readLine();

        // Koniec rozgrywki
        if (line.equals("END")) {
            CheckersServerThread.playingGame = false;
            outLocal.println("END");
            CheckersServerThread.end();
        }
        // Przyjmowanie współrzędnych wykonanego ruchu
        String[] coordinates = line.split(" ");
        first_click[0] = Integer.parseInt(coordinates[0]);
        first_click[1] = Integer.parseInt(coordinates[1]);
        second_click[0] = Integer.parseInt(coordinates[2]);
        second_click[1] = Integer.parseInt(coordinates[3]);

        // Wypisywanie na serwerze
        System.out.println("Coordinates of the first click:");
        System.out.println("X:= " + first_click[0] + ", Y:= " + first_click[1]);
        System.out.println("Coordinates of the second click:");
        System.out.println("X:= " + second_click[0] + ", Y:= " + second_click[1]);

        // Wysyłanie do socketów

        // Jeżeli ruch jest legalny, to wysyłane jest "0" do gracza, który zrobił dany ruch
        outLocal.println("0");
        // Wysyłanie ruchu do przeciwnika
        outOpp.println(first_click[0] + " " + first_click[1] + " " + second_click[0] + " " + second_click[1] + " " + coordinates[4] + " " + coordinates[5]);

        if (Integer.parseInt(coordinates[4])!=2) {
            switchTurn();
        }

    }

    /**
     * Metoda wykonywana przy rozpoczęciu wątku
     */
    @Override
    public void run() {
        try {
            // Inicjalizacja pobierania od socketa dla player1
            InputStream inputF = firstPlayer.getInputStream();
            BufferedReader inF = new BufferedReader(new InputStreamReader(inputF));

            // Inicjalizacja pobierania od socketa dla player2
            InputStream inputS = secondPlayer.getInputStream();
            BufferedReader inS = new BufferedReader(new InputStreamReader(inputS));

            // Inicjalizacja wysyłania do socketa dla player1
            OutputStream outputF = firstPlayer.getOutputStream();
            PrintWriter outF = new PrintWriter(outputF, true);

            // Inicjalizacja wysyłania do socketa dla player2
            OutputStream outputS = secondPlayer.getOutputStream();
            PrintWriter outS = new PrintWriter(outputS, true);

            // W zależności od tury gracza, odpowiednie argumenty przesyłane są do metody odpowiedzialnej za wysyłanie i odbieranie ruchów
            while (true) {
                if (turn==SECOND) {
                    gameState(inS,outS,outF);
                }

                if (turn==FIRST) {
                    gameState(inF,outF,outS);
                }
            }

        }
        catch (IOException ex) {
            System.err.println("ex");
        }
    }
}
