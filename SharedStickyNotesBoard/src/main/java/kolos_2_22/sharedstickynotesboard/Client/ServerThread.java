package kolos_2_22.sharedstickynotesboard.Client;

import kolos_2_22.sharedstickynotesboard.Note;
import kolos_2_22.sharedstickynotesboard.Server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.function.Consumer;

public class ServerThread extends Thread{
    private final Socket socket;
    private PrintWriter out;
    private Consumer<Note> noteConsumer = s -> {};

    public ServerThread(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String line;
            while((line = in.readLine()) != null){
                Note note = Note.fromMessage(line);
                noteConsumer.accept(note);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(Note noteMessage){
        out.println(noteMessage);
    }

    public void setNoteConsumer(Consumer<Note> consumer){
        this.noteConsumer = consumer;
    }

}
