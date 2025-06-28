package kolos_2.kolokwium2021.Server;

import javafx.scene.paint.Color;
import kolos_2.kolokwium2021.DrawController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final DrawController controller;
    private Color currentColor = Color.BLACK;

    public ClientHandler(Socket socket, DrawController controller) {
        this.socket = socket;
        this.controller = controller;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()))) {

            String line;
            while ((line = in.readLine()) != null) {
                line = line.trim();

                if (line.matches("#?[0-9a-fA-F]{6}")) {
                    // Color message
                    if (!line.startsWith("#")) line = "#" + line;
                    currentColor = Color.web(line);
                } else {
                    // Line message
                    String[] parts = line.split(" ");
                    if (parts.length == 4) {
                        double x1 = Double.parseDouble(parts[0]);
                        double y1 = Double.parseDouble(parts[1]);
                        double x2 = Double.parseDouble(parts[2]);
                        double y2 = Double.parseDouble(parts[3]);

                        controller.drawLine(x1, y1, x2, y2, currentColor);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Client disconnected");
        }
    }
}
