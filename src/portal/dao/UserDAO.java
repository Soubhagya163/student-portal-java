package portal.dao;

import portal.db.DB;
import java.sql.*;

public class UserDAO {

    public boolean createStudentUser(String username, String passwordHash) {
        String sql = "INSERT INTO users (username, pass_hash, role) VALUES (?, ?, 'student')";
        try (Connection con = DB.get();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, passwordHash);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            // likely duplicate username
            return false;
        }
    }

    public String validateLogin(String username, String passwordHash) {
        String sql = "SELECT role FROM users WHERE username=? AND pass_hash=?";
        try (Connection con = DB.get();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, passwordHash);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("role");
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public boolean deleteUser(String username) {
        String sql = "DELETE FROM users WHERE username=?";
        try (Connection con = DB.get();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { return false; }
    }
}
