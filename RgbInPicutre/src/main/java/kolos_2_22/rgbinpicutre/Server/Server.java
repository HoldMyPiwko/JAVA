package kolos_2_22.rgbinpicutre.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server extends Thread{
    private static final List<ClientThread> clients = new CopyOnWriteArrayList<>();


    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Serwer działa na porcie 5000");

            while (true) {
                Socket accepted = serverSocket.accept();
                System.out.println("Połączono z klientem " + accepted.getInetAddress());

                ClientThread clientThread = new ClientThread(accepted);
                clients.add(clientThread);
                clientThread.start();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void broadcast(String message){
        for (ClientThread clientThread : clients){
            clientThread.send(message);
        }
    }

    public static void removeClient(ClientThread client){
        clients.remove(client);
    }

}
