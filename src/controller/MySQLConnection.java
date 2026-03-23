package controller;

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
            }catch (Exception e) {
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
            System.out.println("Query execution failed!");
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
            System.out.println("Update execution failed!");
            e.printStackTrace();
        }
        return 0;
    }
    // Close the connection
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("Connection closed.");
            } catch (SQLException e) {
                System.out.println("Failed to close the connection!");
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        // Example usage
        Connection conn = MySQLConnection.getConnection();
//        ResultSet rs = MySQLConnection.executeQuery("SELECT * FROM employees limit 10"); // Change to your table name and query
//        try {
//            while (rs != null && rs.next()) {
//                System.out.println("Employee ID: " + rs.getInt("employee_id") + ", Name: " + rs.getString("first_name"));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            if (rs != null) {
//                try {
//                    rs.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
        ResultSet rs = MySQLConnection.executeQuery("SELECT * FROM cars "); // Change to your table name and query
        try {
            while (rs != null && rs.next()) {
                System.out.println("Car ID: " + rs.getInt("car_id") + ", Model: " + rs.getString("model"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
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
