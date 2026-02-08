package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuItemDAO {

    // CREATE
    public static void add(MenuItem item) {
        String sql = """
            INSERT INTO menu_items (name, price, category, type, vegetarian, has_ice)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = dbconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            conn.setAutoCommit(true);

            ps.setString(1, item.getName());
            ps.setDouble(2, item.getPrice());
            ps.setString(3, item.getCategory().name());

            if (item instanceof Dish d) {
                ps.setString(4, "DISH");
                ps.setBoolean(5, d.isVegetarian());
                ps.setNull(6, Types.BOOLEAN);
            }
            else if (item instanceof Drink dr) {
                ps.setString(4, "DRINK");
                ps.setNull(5, Types.BOOLEAN);
                ps.setBoolean(6, dr.hasIce());
            }
            else {
                throw new IllegalArgumentException("Unknown MenuItem type");
            }

            int rows = ps.executeUpdate();
            if (rows == 0) {
                System.out.println("Warning: record was not added!");
            }

        } catch (Exception e) {
            System.out.println("Error while inserting into database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // READ
    public static List<MenuItem> getAll() {
        List<MenuItem> list = new ArrayList<>();
        String sql = "SELECT * FROM menu_items";

        try (Connection conn = dbconnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                Category category = Category.valueOf(rs.getString("category"));
                String type = rs.getString("type");

                if ("DISH".equals(type)) {
                    boolean vegetarian = rs.getBoolean("vegetarian");
                    list.add(new Dish(id, name, price, vegetarian, category));
                }
                else if ("DRINK".equals(type)) {
                    boolean hasIce = rs.getBoolean("has_ice");
                    list.add(new Drink(id, name, price, hasIce, category));
                }
            }

        } catch (Exception e) {
            System.out.println("Error while reading from database: " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    // DELETE
    public static void deleteById(int id) {
        String sql = "DELETE FROM menu_items WHERE id = ?";

        try (Connection conn = dbconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Item deleted.");
            } else {
                System.out.println("ID not found.");
            }

        } catch (Exception e) {
            System.out.println("Error while deleting item: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // UPDATE
    public static void update(MenuItem item) {
        String sql = """
            UPDATE menu_items
            SET name = ?, price = ?, category = ?, vegetarian = ?, has_ice = ?
            WHERE id = ?
        """;

        try (Connection conn = dbconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, item.getName());
            ps.setDouble(2, item.getPrice());
            ps.setString(3, item.getCategory().name());

            if (item instanceof Dish d) {
                ps.setBoolean(4, d.isVegetarian());
                ps.setNull(5, Types.BOOLEAN);
            }
            else if (item instanceof Drink dr) {
                ps.setNull(4, Types.BOOLEAN);
                ps.setBoolean(5, dr.hasIce());
            }

            ps.setInt(6, item.getId());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Item updated.");
            } else {
                System.out.println("ID not found.");
            }

        } catch (Exception e) {
            System.out.println("Error while updating item: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
