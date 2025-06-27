package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread{
    private final Socket socket;
    private final PrintWriter out;
    private final BufferedReader in;

    public ClientThread(Socket socket) throws IOException {
        this.socket = socket;
        out = new PrintWriter(socket.getOutputStream());
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void send(String message){
        out.println(message);
        out.flush();
    }

    public void run(){
        System.out.println("Wątek wystartował! Czytanie...");
        try {
            String message;
            while ((message =in.readLine()) != null){
                System.out.println("Otrzymano: " + message);
                Server.broadcast(message);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                socket.close();
            } catch (IOException _) {}
            Server.removeClient(this);
        }
    }

    public Socket getSocket(){
        return socket;
    }

}
