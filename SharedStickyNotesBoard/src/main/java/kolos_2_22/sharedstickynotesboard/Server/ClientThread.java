package kolos_2_22.sharedstickynotesboard.Server;

import kolos_2_22.sharedstickynotesboard.Note;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class ClientThread extends Thread {
    private final Server server;
    private final Socket socket;
    private PrintWriter out;

    public ClientThread(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            out = new PrintWriter(socket.getOutputStream(), true);

            String line;
            while ((line = in.readLine()) != null){
                server.broadcast(line);
                System.out.println("received and broadcast: " + line);
            }
        } catch (IOException e) {
            System.err.println("Client disconnected: " + socket);
        } finally {
            server.removeClient(this);
        }
    }


    public void send(Note note) {
            out.println(Note.toMessage(note.x(), note.y(), note.text(), note.color()));
        }
    }

