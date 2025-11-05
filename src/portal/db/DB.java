package portal.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DB {

    private static final String URL = "jdbc:mysql://localhost:3306/student_portal";
    private static final String USER = "root";
    private static final String PASSWORD = "bunty2003"; // <-- change this

    public static Connection get() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
