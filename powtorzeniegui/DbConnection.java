package pl.umcs.oop.powtorzeniegui;

import javafx.scene.paint.Color;

import java.sql.*;

public class DbConnection {
    static String url = "jdbc:sqlite:data.db";
    public static Connection connection = null;

    public static Connection getConnection() {
        return connection;
    }

    static public void connect() {
        try {
            connection = DriverManager. getConnection(url);
            if (connection != null){
                createTable();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS dot(\n" +
                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "x INTEGER NOT NULL,\n" +
                "y INTEGER NOT NULL,\n" +
                "color TEXT NOT NULL,\n" +
                "radius INTEGER NOT NULL\n" +
                ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static public void insertValues(double x, double y, Color dotColor, double radius) {
        String sql = "INSERT INTO dot(x,y,color,radius) VALUES(?, ?, ?, ?)";
        try (PreparedStatement ptsmt = connection.prepareStatement(sql)) {
            ptsmt.setDouble(1, x);
            ptsmt.setDouble(2, y);
            ptsmt.setString(3, dotColor.toString());
            ptsmt.setDouble(4, radius);
            ptsmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
