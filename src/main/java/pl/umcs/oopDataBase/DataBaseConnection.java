package pl.umcs.oopDataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }


    public void connect(String dbPath) {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
        } catch (SQLException e){
            throw new RuntimeException();
        }
    }

    public void disconnect(){
        try {
            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
