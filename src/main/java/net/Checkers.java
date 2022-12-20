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
        this.secondPlayer= secondPlayer;
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

            //outF.println("1");
            //outS.println("2");

            String line;
            do {
                if (turn==SECOND) {
                    // Odbieranie od socketa
                    line = inS.readLine();
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
                    outS.println("0");
                    //Sending moves to another player
                    outF.println(first_click[0] + " " + first_click[1] + " " + second_click[0] + " " + second_click[1]);
                    turn=FIRST;
                }

                if (turn==FIRST) {
                    // Odbieranie od socketa
                    line = inF.readLine();
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
                    outF.println("0");
                    //Sending moves to another player
                    outS.println(first_click[0] + " " + first_click[1] + " " + second_click[0] + " " + second_click[1]);
                    turn=SECOND;
                }
            } while (true);

        }
        catch (IOException ex) {
            System.err.println("ex");
        }
    }
}
