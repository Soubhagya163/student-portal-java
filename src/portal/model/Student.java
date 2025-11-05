package portal.model;

public class Student {
    private int id;
    private String username;
    private String fullName;
    private String email;
    private String phone;

    // Constructor
    public Student(int id, String username, String fullName, String email, String phone) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
    }

    // Getters
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }

    // toString method for clean display
    @Override
    public String toString() {
        return id + " | " + username + " | " + fullName + " | " + email + " | " + phone;
    }
}
