package portal.dao;

import portal.db.DB;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class AttendanceDAO {

    // Get attendance by student_id
    public Map<String, Integer> getAttendanceByStudentId(int studentId) {
        Map<String, Integer> att = new HashMap<>();
        String sql = "SELECT month_year, percentage FROM attendance WHERE student_id=?";
        try (Connection con = DB.get();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    att.put(rs.getString("month_year"), rs.getInt("percentage"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return att;
    }

    // Insert or update attendance record   
    public boolean upsertAttendance(int studentId, String month, int percentage) {
        String sql = """
                INSERT INTO attendance (student_id, month_year, percentage)
                VALUES (?, ?, ?)
                ON DUPLICATE KEY UPDATE percentage=VALUES(percentage)
                """;
        try (Connection con = DB.get();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setString(2, month);
            ps.setInt(3, percentage);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
