package kolos_2_22.rgbinpicutre.Server;

import java.io.*;
import java.net.Socket;

public class ClientThread extends Thread{
    private final Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public ClientThread(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream());
    }

    public void send(String message){
        out.println(message);
        out.flush();
    }

    @Override
    public void run() {
        System.out.println("Wątek wystartował! Czytanie...");
        try  {
            String line;
            while((line = in.readLine()) != null){
                System.out.println("Odebrano od klienta: " + line);
                Server.broadcast(line);

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Błąd przy zamykaniu gniazda" + e.getMessage());

            }
        }
    }
}
