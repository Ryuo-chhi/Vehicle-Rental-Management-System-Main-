package com.rental.system.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import java.sql.ResultSet;

public class MySQLConnection {

    private static Connection connection = null;
    private static final String URL = getEnv("DB_URL", "jdbc:mysql://localhost:3306/rental_db");
    private static final String USERNAME = getEnv("DB_USERNAME", "root");
    private static final String PASSWORD = getEnv("DB_PASSWORD", "");

    private static String getEnv(String key, String defaultValue) {
        String value = System.getenv(key);
        if (value != null) {
            return value;
        }
        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(".env"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                int eqIdx = line.indexOf('=');
                if (eqIdx > 0) {
                    String k = line.substring(0, eqIdx).trim();
                    if (k.equals(key)) {
                        String v = line.substring(eqIdx + 1).trim();
                        if ((v.startsWith("\"") && v.endsWith("\"")) || (v.startsWith("'") && v.endsWith("'"))) {
                            v = v.substring(1, v.length() - 1);
                        }
                        return v;
                    }
                }
            }
        } catch (java.io.IOException e) {
            // Ignore if .env is missing or unreadable
        }
        return defaultValue;
    }

    // Establish the connection
    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("Connected to MySQL successfully!");
                initializeTables(connection);
            } catch (SQLException e) {
                System.out.println("Failed to connect to MySQL com.rental.system.database.");
                System.out.println("Reason: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return connection;
    }

    private static void initializeTables(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            // 1. maintenance_records table
            stmt.execute("CREATE TABLE IF NOT EXISTS maintenance_records (" +
                    "maintenance_id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "vehicle_id INT NOT NULL, " +
                    "details VARCHAR(255) NOT NULL, " +
                    "cost DECIMAL(10,2) NOT NULL DEFAULT 0, " +
                    "start_date VARCHAR(20) NOT NULL, " +
                    "end_date VARCHAR(20), " +
                    "status VARCHAR(20) NOT NULL DEFAULT 'ONGOING', " +
                    "FOREIGN KEY (vehicle_id) REFERENCES vehicles(vehicle_id) ON DELETE CASCADE" +
                    ")");

            // 2. promotions table
            stmt.execute("CREATE TABLE IF NOT EXISTS promotions (" +
                    "promo_id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "code VARCHAR(50) UNIQUE NOT NULL, " +
                    "discount_percent DECIMAL(5,2) NOT NULL, " +
                    "is_active BOOLEAN NOT NULL DEFAULT TRUE" +
                    ")");

            // 3. system_settings table
            stmt.execute("CREATE TABLE IF NOT EXISTS system_settings (" +
                    "setting_key VARCHAR(50) PRIMARY KEY, " +
                    "setting_value VARCHAR(50) NOT NULL" +
                    ")");

            // Insert default settings if they are not present
            stmt.execute("INSERT IGNORE INTO system_settings (setting_key, setting_value) VALUES " +
                    "('TAX_RATE', '0.0'), " +
                    "('LATE_PENALTY_MULTIPLIER', '1.5'), " +
                    "('MAX_RENTAL_DURATION', '30')");
        } catch (SQLException e) {
            System.out.println("Failed to initialize database tables: " + e.getMessage());
        }
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
                System.out.println("Customer ID: " + rs.getInt("customer_id") + ", Customer Name: "
                        + rs.getString("customer_name"));
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
