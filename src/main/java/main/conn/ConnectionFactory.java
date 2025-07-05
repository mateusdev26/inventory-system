package main.conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionFactory {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/stock";
        String password = "ASD123as";
        String user = "root";
        return DriverManager.getConnection(url, user, password);

    }


}