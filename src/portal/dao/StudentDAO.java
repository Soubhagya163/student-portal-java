package portal.dao;

import portal.db.DB;
import portal.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    // Find student by username
    public Student findByUsername(String username) {
        String sql = "SELECT * FROM students WHERE username=?";
        try (Connection con = DB.get();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Find student by ID
    public Student findById(int id) {
        String sql = "SELECT * FROM students WHERE id=?";
        try (Connection con = DB.get();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Fetch all students
    public List<Student> findAll() {
        String sql = "SELECT * FROM students ORDER BY id";
        List<Student> list = new ArrayList<>();
        try (Connection con = DB.get();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Add new student record
    public boolean addStudent(Student s) {
        String sql = "INSERT INTO students (username, full_name, email, phone) VALUES (?, ?, ?, ?)";
        try (Connection con = DB.get();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, s.getUsername());
            ps.setString(2, s.getFullName());
            ps.setString(3, s.getEmail());
            ps.setString(4, s.getPhone());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) s.setId(keys.getInt(1));
                }
                return true;
            }
        } catch (SQLIntegrityConstraintViolationException dup) {
            System.out.println("[WARN] Username or email already exists.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Update student details
    public boolean updateProfile(String username, String fullName, String email, String phone) {
        String sql = "UPDATE students SET full_name=?, email=?, phone=? WHERE username=?";
        try (Connection con = DB.get();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, fullName);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, username);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete student record
    public boolean deleteStudentByUsername(String username) {
        String sql = "DELETE FROM students WHERE username=?";
        try (Connection con = DB.get();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Mapping database row to Java object
    private Student map(ResultSet rs) throws Exception {
        return new Student(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("full_name"),
                rs.getString("email"),
                rs.getString("phone")
        );
    }
}
