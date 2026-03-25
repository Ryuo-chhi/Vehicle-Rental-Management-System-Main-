package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import java.sql.ResultSet;

public class MySQLConnection {

    private static Connection connection = null;
    private static final String URL = System.getenv("DB_URL");
    private static final String USERNAME = System.getenv("DB_USERNAME");
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    // Establish the connection
    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("Connected to MySQL successfully!");
            } catch (SQLException e) {
                System.out.println("Failed to connect to MySQL database.");
                System.out.println("Reason: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return connection;
    }

    // Execute a query (SELECT)
    public static ResultSet executeQuery(String query) {
        try {
            Statement statement = getConnection().createStatement();
            return statement.executeQuery(query);
        } catch (SQLException e) {
            System.out.println("Query execution failed.");
            System.out.println("Query: " + query);
            System.out.println("Reason: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Execute an update (INSERT, UPDATE, DELETE)
    public static int executeUpdate(String query) {
        try {
            Statement statement = getConnection().createStatement();
            return statement.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println("Update execution failed.");
            System.out.println("Query: " + query);
            System.out.println("Reason: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    // Execute an insert and retrieve the Auto-Incremented Database ID
    public static int executeInsertAndGetId(String query) {
        try {
            Statement statement = getConnection().createStatement();
            statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // Return the newly generated ID
            }
        } catch (SQLException e) {
            System.out.println("Insert execution failed or failed to retrieve generated key.");
            System.out.println("Query: " + query);
            System.out.println("Reason: " + e.getMessage());
            e.printStackTrace();
        }
        return -1; // -1 indicates failure
    }

    // Close the connection
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("Connection closed.");
            } catch (SQLException e) {
                System.out.println("Failed to close the connection.");
                System.out.println("Reason: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        // Example usage
        Connection conn = MySQLConnection.getConnection();
        ResultSet rs = MySQLConnection.executeQuery("SELECT * FROM customers "); // Change to your table name and query
        try {
            while (rs != null && rs.next()) {
                System.out.println("Customer ID: " + rs.getInt("customer_id") + ", Customer Name: " + rs.getString("customer_name"));
            }
        } catch (SQLException e) {
            System.out.println("Failed while reading query results.");
            System.out.println("Reason: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.out.println("Failed to close ResultSet.");
                    System.out.println("Reason: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        if (conn != null) {
            System.out.println("Connection established successfully!");
            MySQLConnection.closeConnection();
        } else {
            System.out.println("Failed to establish connection.");
        }
    }
}
