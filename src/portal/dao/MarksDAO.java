package portal.dao;

import portal.db.DB;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class MarksDAO {

    // Get marks for a student (by student_id)
    public Map<String, Integer> getMarksByStudentId(int studentId) {
        Map<String, Integer> marks = new HashMap<>();
        String sql = "SELECT subject, score FROM marks WHERE student_id=?";
        try (Connection con = DB.get();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    marks.put(rs.getString("subject"), rs.getInt("score"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return marks;
    }

    // Insert or update mark for a subject
    public boolean upsertMark(int studentId, String subject, int score) {
        String sql = """
                INSERT INTO marks (student_id, subject, score)
                VALUES (?, ?, ?)
                ON DUPLICATE KEY UPDATE score=VALUES(score)
                """;
        try (Connection con = DB.get();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setString(2, subject);
            ps.setInt(3, score);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
