package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Admin;
import util.DBUtil;

public class AdminDao {

    public Admin authenticate(String name, String password) throws SQLException {
        String sql = "SELECT admin_id, name, password FROM admin WHERE name = ? AND password = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Admin(
                        rs.getInt("admin_id"),
                        rs.getString("name"),
                        rs.getString("password")
                    );
                }
            }
        }
        return null;
    }
}
