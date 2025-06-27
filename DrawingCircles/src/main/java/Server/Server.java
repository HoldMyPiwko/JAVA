package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server extends Thread{
    private static final List<ClientThread> clients = new CopyOnWriteArrayList<>();


    public static void main(String[] args) throws IOException {
        try(ServerSocket serverSocket = new ServerSocket(5000)){



            while(true){
                System.out.println("Czekam na połączenie...");
                Socket accepted = serverSocket.accept();
                System.out.println("Zaakceptowano: " + accepted);

                ClientThread clientThread = new ClientThread(accepted);
                clients.add(clientThread);
                clientThread.start();

            }

        } catch (IOException e){
            throw new IOException();
        }
    }

    public static void broadcast(String message){
        for (ClientThread clientThread : clients){
            clientThread.send(message);
        }
    }

    public static void removeClient(ClientThread client){
        System.out.println("Usuwanie klient: " + client.getSocket());
        clients.remove(client);
    }


}
