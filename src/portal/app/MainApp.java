package portal.app;

import portal.dao.*;
import portal.model.Student;
import portal.util.*;

import java.util.*;

public class MainApp {
    private static final UserDAO userDAO = new UserDAO();
    private static final StudentDAO studentDAO = new StudentDAO();
    private static final MarksDAO marksDAO = new MarksDAO();
    private static final AttendanceDAO attendanceDAO = new AttendanceDAO();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("==========================================");
        System.out.println("       🎓 STUDENT PORTAL SYSTEM 🎓");
        System.out.println("==========================================");

        while (true) {
            System.out.println("\n1. Login as Admin");
            System.out.println("2. Login as Student");
            System.out.println("3. Exit");
            //System.out.print("Enter choice: ");
            int choice = InputUtil.readInt("Enter choice: ");


            switch (choice) {
                case 1 -> adminLogin();
                case 2 -> studentLogin();
                case 3 -> {
                    System.out.println("Thank you for using Student Portal. Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice! Try again.");
            }
        }
    }

    // ==============================
    // ADMIN LOGIN
    // ==============================
    private static void adminLogin() {
        String username = InputUtil.readNonEmpty("Admin username: ");
        String password = InputUtil.readNonEmpty("Password: ");
        String hash = PasswordUtil.sha256Hex(password);

        String role = userDAO.validateLogin(username, hash);
        if (role != null && role.equals("admin")) {
            System.out.println("\n✅ Admin login successful!");
            adminMenu();
        } else {
            System.out.println("❌ Invalid admin credentials!");
        }
    }

    // ==============================
    // STUDENT LOGIN
    // ==============================
    private static void studentLogin() {
        String username = InputUtil.readNonEmpty("Student username: ");
        String password = InputUtil.readNonEmpty("Password: ");
        String hash = PasswordUtil.sha256Hex(password);

        String role = userDAO.validateLogin(username, hash);
        if (role != null && role.equals("student")) {
            System.out.println("\n✅ Login successful! Welcome, " + username);
            Student s = studentDAO.findByUsername(username);
            if (s != null) studentMenu(s);
            else System.out.println("Student profile not found!");
        } else {
            System.out.println("❌ Invalid credentials!");
        }
    }

    // ==============================
    // ADMIN MENU
    // ==============================
    private static void adminMenu() {
        while (true) {
            System.out.println("\n========= ADMIN DASHBOARD =========");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Update Student Profile");
            System.out.println("4. Delete Student");
            System.out.println("5. Add/Update Marks");
            System.out.println("6. Add/Update Attendance");
            System.out.println("7. Logout");

            int choice = InputUtil.readInt("Enter your choice: ");

            switch (choice) {
                case 1 -> addStudentFlow();
                case 2 -> viewAllStudents();
                case 3 -> updateStudentProfile();
                case 4 -> deleteStudent();
                case 5 -> addOrUpdateMarks();
                case 6 -> addOrUpdateAttendance();
                case 7 -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid choice! Try again.");
            }
        }
    }

    // ==============================
    // STUDENT MENU
    // ==============================
    private static void studentMenu(Student s) {
        while (true) {
            System.out.println("\n========= STUDENT DASHBOARD =========");
            System.out.println("1. View Profile");
            System.out.println("2. View Marks");
            System.out.println("3. View Attendance");
            System.out.println("4. Logout");

            int choice = InputUtil.readInt("Enter your choice: ");

            switch (choice) {
                case 1 -> System.out.println("\nYour Profile:\n" + s);
                case 2 -> viewMarks(s.getId());
                case 3 -> viewAttendance(s.getId());
                case 4 -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid choice! Try again.");
            }
        }
    }

    // ==============================
    // ADMIN FUNCTIONS
    // ==============================
    private static void addStudentFlow() {
        String username = InputUtil.readNonEmpty("New username: ");
        String password = InputUtil.readNonEmpty("Temp password for student: ");
        String fullName = InputUtil.readNonEmpty("Full name: ");
        String email = InputUtil.readNonEmpty("Email: ");
        String phone = InputUtil.readNonEmpty("Phone: ");

        String hash = PasswordUtil.sha256Hex(password);

        boolean userOk = userDAO.createStudentUser(username, hash);
        Student s = new Student(0, username, fullName, email, phone);
        boolean profOk = studentDAO.addStudent(s);

        if (userOk && profOk)
            System.out.println("✅ Student created successfully!");
        else {
            System.out.println("❌ Failed to create student. Rolling back...");
            if (userOk && !profOk) userDAO.deleteUser(username);
        }
    }

    private static void viewAllStudents() {
        System.out.println("\n========= STUDENT LIST =========");
        List<Student> students = studentDAO.findAll();
        if (students.isEmpty()) System.out.println("No students found.");
        else students.forEach(System.out::println);
    }

    private static void updateStudentProfile() {
        String username = InputUtil.readNonEmpty("Enter username to update: ");
        Student s = studentDAO.findByUsername(username);
        if (s == null) {
            System.out.println("Student not found!");
            return;
        }

        String fullName = InputUtil.readNonEmpty("New full name: ");
        String email = InputUtil.readNonEmpty("New email: ");
        String phone = InputUtil.readNonEmpty("New phone: ");

        if (studentDAO.updateProfile(username, fullName, email, phone))
            System.out.println("✅ Student profile updated successfully.");
        else
            System.out.println("❌ Update failed.");
    }

    private static void deleteStudent() {
        String username = InputUtil.readNonEmpty("Enter username to delete: ");
        if (studentDAO.deleteStudentByUsername(username)) {
            userDAO.deleteUser(username);
            System.out.println("✅ Student deleted successfully.");
        } else {
            System.out.println("❌ Failed to delete student.");
        }
    }

    private static void addOrUpdateMarks() {
        int id = InputUtil.readInt("Enter student ID: ");
        String subject = InputUtil.readNonEmpty("Enter subject: ");
        int score = InputUtil.readInt("Enter score: ");

        if (marksDAO.upsertMark(id, subject, score))
            System.out.println("✅ Marks updated successfully.");
        else
            System.out.println("❌ Failed to update marks.");
    }

    private static void addOrUpdateAttendance() {
        int id = InputUtil.readInt("Enter student ID: ");
        String month = InputUtil.readNonEmpty("Enter month: ");
        int percentage = InputUtil.readInt("Enter attendance %: ");

        if (attendanceDAO.upsertAttendance(id, month, percentage))
            System.out.println("✅ Attendance updated successfully.");
        else
            System.out.println("❌ Failed to update attendance.");
    }

    // ==============================
    // STUDENT FUNCTIONS
    // ==============================
    private static void viewMarks(int studentId) {
        System.out.println("\n========= MARKS =========");
        Map<String, Integer> marks = marksDAO.getMarksByStudentId(studentId);
        if (marks.isEmpty()) System.out.println("No marks found.");
        else marks.forEach((subject, score) -> System.out.println(subject + ": " + score));
    }

    private static void viewAttendance(int studentId) {
        System.out.println("\n========= ATTENDANCE =========");
        Map<String, Integer> att = attendanceDAO.getAttendanceByStudentId(studentId);
        if (att.isEmpty()) System.out.println("No attendance record found.");
        else att.forEach((month, percent) -> System.out.println(month + ": " + percent + "%"));
    }
}
