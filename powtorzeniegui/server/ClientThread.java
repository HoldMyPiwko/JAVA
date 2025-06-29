package pl.umcs.oop.powtorzeniegui.server;

import pl.umcs.oop.powtorzeniegui.DbConnection;
import pl.umcs.oop.powtorzeniegui.Dot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {
    private final Socket socket;
    private final PrintWriter out;
    private final BufferedReader in;

    public ClientThread(Socket socket) throws IOException {
        this.socket = socket;
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void send(String message) {
        out.println(message);
    }


    @Override
    public void run() {
        System.out.println(socket + "wątek wystartował! Czytanie...");
        for (Dot dot : Server.getSavedDots()) {
            send(dot.toMessage());
        }

        try {
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Otrzymano: " + message);
                Server.broadcast(message);

                if (message.startsWith("DRAW")){
                    Dot dot = Dot.fromMessage(message);
                    Server.saveDot(dot);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
