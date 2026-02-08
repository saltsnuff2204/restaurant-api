package org.example;
import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInit {
    public static void createTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS menu_items (
                id SERIAL PRIMARY KEY,
                name VARCHAR(100),
                price DOUBLE PRECISION,
                category VARCHAR(50),
                type VARCHAR(20),
                vegetarian BOOLEAN,
                has_ice BOOLEAN
            );
        """;

        try (Connection conn = dbconnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table menu_items created (if it did not exist).");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
