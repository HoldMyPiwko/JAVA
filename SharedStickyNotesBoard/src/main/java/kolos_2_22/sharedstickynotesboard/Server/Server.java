package kolos_2_22.sharedstickynotesboard.Server;


import kolos_2_22.sharedstickynotesboard.Note;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server {

    private final List<ClientThread> clients = Collections.synchronizedList(new ArrayList<>());
    private ServerSocket serverSocket;
    private int port = 5000;

    public void start() throws IOException {

        serverSocket = new ServerSocket(port);
        System.out.println("Serwer działa na porcie" + port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            ClientThread clientThread = new ClientThread(this, clientSocket);
            clients.add(clientThread);
            clientThread.start();
        }
    }

    public void broadcast(String message) {
        for (ClientThread clientThread : clients) {
            clientThread.send(Note.toMessage());
        }
    }

    public void removeClient(ClientThread clientThread){
        clients.remove(clientThread);
    }

    public static void main(String[] args) throws IOException {
        new Server().start();
    }
}

