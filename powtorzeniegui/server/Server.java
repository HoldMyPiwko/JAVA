package pl.umcs.oop.powtorzeniegui.server;

import javafx.scene.paint.Color;
import pl.umcs.oop.powtorzeniegui.DbConnection;
import pl.umcs.oop.powtorzeniegui.Dot;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static pl.umcs.oop.powtorzeniegui.DbConnection.connection;

public class Server extends Thread {
    private static final List<ClientThread> clients = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {
        new Thread(DbConnection::connect).start();
        try (ServerSocket serverSocket = new ServerSocket(5000)) {

            System.out.println("Serwer wystartowal");
            while (true) {
                System.out.println("Oczekiwanie na połączenie");
                Socket accepted = serverSocket.accept();
                System.out.println("Połączono: " + accepted);

                ClientThread ct = new ClientThread(accepted);
                clients.add(ct);
                ct.setDaemon(true);
                ct.start();
            }


        } catch (IOException e) {
            throw new RuntimeException();
        }
    }


    public static void broadcast(String message) {
        for (ClientThread clients : clients) {
            clients.send(message);
        }

    }

    public static void saveDot(Dot dot){
        DbConnection.insertValues(dot.x(), dot.y(), dot.circleColor(), dot.r());
    }

    public static List<Dot> getSavedDots(){
        DbConnection.connect();
        String sql = "SELECT * FROM dot";
        List<Dot> dots = new ArrayList<>();
        try (Statement stmt = DbConnection.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                double x = rs.getDouble("x");
                double y = rs.getDouble("y");
                Color color = Color.valueOf(rs.getString("color"));
                double radius = rs.getDouble("r");

                dots.add(new Dot(x,y,radius,color));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dots;
    }
}
