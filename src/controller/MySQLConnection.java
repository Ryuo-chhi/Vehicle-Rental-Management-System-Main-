package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLConnection {

    private static Connection connection = null;
    private static final String URL = "jdbc:mysql://localhost:3306/hr"; // Change to your database URL
    private static final String USERNAME = "root";
    private static final String PASSWORD = "IDTB110290";

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
        connection = getConnection();
        ResultSet rs = executeQuery("select * from employees limit 10;");
        try {
            while (rs != null && rs.next()) {
                System.out.println("Employee ID: " + rs.getInt("employee_id") + ", Name: " + rs.getString("first_name"));
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }
}

