package net;

import java.io.*;
import java.net.*;

public class CheckersServerThread
{
    public static void main(String[] args)
    {
        try (ServerSocket serverSocket = new ServerSocket(4445)) {

            System.out.println("Server is listening on port 4445");

            while (true) {
                Socket firstClient = serverSocket.accept();
                System.out.println("First client connected");
                System.out.println("Waiting for the second player");

                Socket secondClient = serverSocket.accept();
                System.out.println("Second client connected");

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