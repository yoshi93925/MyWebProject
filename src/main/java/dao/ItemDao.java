package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Item;
import util.DBUtil;

public class ItemDao {

    public List<Item> findAll() throws SQLException {
        List<Item> list = new ArrayList<>();
        String sql = "SELECT item_id, item_name, stock, location, genre_id, updated_at FROM items ORDER BY item_id ASC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Item(
                    rs.getInt("item_id"),
                    rs.getString("item_name"),
                    rs.getInt("stock"),
                    rs.getString("location"),
                    rs.getInt("genre_id"),
                    rs.getTimestamp("updated_at")
                ));
            }
        }
        return list;
    }

    public List<Item> findAvailable() throws SQLException {
        List<Item> list = new ArrayList<>();
        String sql = "SELECT item_id, item_name, stock, location, genre_id, updated_at FROM items WHERE stock > 0 ORDER BY genre_id ASC, item_id ASC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Item(
                    rs.getInt("item_id"),
                    rs.getString("item_name"),
                    rs.getInt("stock"),
                    rs.getString("location"),
                    rs.getInt("genre_id"),
                    rs.getTimestamp("updated_at")
                ));
            }
        }
        return list;
    }

    public Item findById(int itemId) throws SQLException {
        String sql = "SELECT item_id, item_name, stock, location, genre_id, updated_at FROM items WHERE item_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, itemId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Item(
                        rs.getInt("item_id"),
                        rs.getString("item_name"),
                        rs.getInt("stock"),
                        rs.getString("location"),
                        rs.getInt("genre_id"),
                        rs.getTimestamp("updated_at")
                    );
                }
            }
        }
        return null;
    }

    public Item findByIdForUpdate(Connection conn, int itemId) throws SQLException {
        String sql = "SELECT item_id, item_name, stock, location, genre_id, updated_at FROM items WHERE item_id = ? FOR UPDATE";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, itemId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Item(
                        rs.getInt("item_id"),
                        rs.getString("item_name"),
                        rs.getInt("stock"),
                        rs.getString("location"),
                        rs.getInt("genre_id"),
                        rs.getTimestamp("updated_at")
                    );
                }
            }
        }
        return null;
    }

    public boolean isDuplicate(int genreId, String itemName, String location, Integer excludeItemId) throws SQLException {
        String sql;
        if (excludeItemId == null) {
            sql = "SELECT COUNT(*) FROM items WHERE genre_id = ? AND item_name = ? AND location = ?";
        } else {
            sql = "SELECT COUNT(*) FROM items WHERE genre_id = ? AND item_name = ? AND location = ? AND item_id != ?";
        }
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, genreId);
            ps.setString(2, itemName);
            ps.setString(3, location);
            if (excludeItemId != null) {
                ps.setInt(4, excludeItemId);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public boolean insert(Item item) throws SQLException {
        String sql = "INSERT INTO items (genre_id, item_name, stock, location, updated_at) VALUES (?, ?, ?, ?, now())";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, item.getGenreId());
            ps.setString(2, item.getItemName());
            ps.setInt(3, item.getStock());
            ps.setString(4, item.getLocation());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean update(Connection conn, Item item) throws SQLException {
        String sql = "UPDATE items SET genre_id = ?, item_name = ?, stock = ?, location = ?, updated_at = now() WHERE item_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, item.getGenreId());
            ps.setString(2, item.getItemName());
            ps.setInt(3, item.getStock());
            ps.setString(4, item.getLocation());
            ps.setInt(5, item.getItemId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean addStock(Connection conn, int itemId, int quantity) throws SQLException {
        String sql = "UPDATE items SET stock = stock + ?, updated_at = now() WHERE item_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, itemId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deductStock(Connection conn, int itemId, int quantity) throws SQLException {
        String sql = "UPDATE items SET stock = stock - ?, updated_at = now() WHERE item_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, itemId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean hasOrderHistory(Connection conn, int itemId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM order_details WHERE item_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, itemId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public boolean delete(Connection conn, int itemId) throws SQLException {
        String sql = "DELETE FROM items WHERE item_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, itemId);
            return ps.executeUpdate() > 0;
        }
    }
}
