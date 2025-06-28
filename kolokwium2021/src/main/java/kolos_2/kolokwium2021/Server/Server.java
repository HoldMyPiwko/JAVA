package kolos_2.kolokwium2021.Server;

import kolos_2.kolokwium2021.DrawController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final DrawController controller;

    public Server(DrawController controller) {
        this.controller = controller;
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Server started on port 5000");
            controller.clear();

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket, controller)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
