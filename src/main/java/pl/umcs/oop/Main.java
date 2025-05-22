package pl.umcs.oop;

import pl.umcs.DataBase.DataBaseConnection;
import pl.umcs.auth.Account;
import pl.umcs.auth.AccountManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) throws SQLException {
        DataBaseConnection db = new DataBaseConnection();
        db.connect("test.db");
        AccountManager am = new AccountManager(db);
        am.register("user2", "pass123");
        System.out.println(am.authenticate("user2", "pass123"));
        System.out.println(am.getAccount(1));
        System.out.println(am.getAccount("user2"));
    }
}