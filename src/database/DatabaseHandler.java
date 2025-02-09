package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHandler {
    private static DatabaseHandler instance;
    private Connection connection;

    private static final String URL = "jdbc:postgresql://localhost:5432/bookstore";
    private static final String USER = "bookstore_user";
    private static final String PASSWORD = "your_password";

    static {
        try {
            Class.forName("org.postgresql.Driver"); // Ensure this line is present
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL JDBC Driver Not Found!", e);
        }
    }



    public DatabaseHandler() {
        connect();
    }

    private void connect() {
        try {
            System.out.println("Attempting to connect to the database...");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connected successfully!");
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to connect to the database!");
            e.printStackTrace();
        }
    }

    public static synchronized DatabaseHandler getInstance() {
        if (instance == null) {
            instance = new DatabaseHandler();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connect();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to reconnect to the database!", e);
        }
        return connection;
    }
}
