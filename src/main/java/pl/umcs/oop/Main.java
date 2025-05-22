package pl.umcs.oop;

import pl.umcs.oopDataBase.DataBaseConnection;

import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        DataBaseConnection db = new DataBaseConnection();
        db.connect("test.db");
        try(Statement statement = db.getConnection().createStatement()) {
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS test(
                        id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                        value TEXT NOT NULL)
                    """);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}