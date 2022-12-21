package net;

import java.io.*;
import java.net.Socket;

public class Checkers implements Runnable {
    private final Socket firstPlayer;
    private final Socket secondPlayer;
    private final static int FIRST=1;
    private final static int SECOND=2;
    private static int turn=FIRST;
    private final int[] first_click = new int[2];
    private final int[] second_click = new int[2];

    public Checkers(Socket firstPlayer, Socket secondPlayer) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
    }
    private void switchTurn() {
        if (turn==SECOND) {
            turn = FIRST;
        } else {
            turn = SECOND;
        }
    }

    private void gameState(BufferedReader inR, PrintWriter outLocal, PrintWriter outOpp) throws IOException {
        // Odbieranie od socketa
        String line = inR.readLine();
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
        // Wysylanie do socketa
        //If move is legal, then send 0.
        //outS.println("0");
        outLocal.println("0");
        //Sending moves to another player
        outOpp.println(first_click[0] + " " + first_click[1] + " " + second_click[0] + " " + second_click[1]);
        switchTurn();
    }
    @Override
    public void run() {
        try {
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
