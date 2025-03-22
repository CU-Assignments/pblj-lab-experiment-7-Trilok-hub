### Instructions for Java JDBC MVC Student Management System  
CODE:
import java.sql.*;
import java.util.Scanner;

class Student {
    private int studentID;
    private String name;
    private String department;
    private double marks;

    public Student(int studentID, String name, String department, double marks) {
        this.studentID = studentID;
        this.name = name;
        this.department = department;
        this.marks = marks;
    }

    public int getStudentID() { return studentID; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public double getMarks() { return marks; }
}

class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/StudentDB";
    private static final String USER = "your_username";
    private static final String PASSWORD = "your_password";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void addStudent(Student student) {
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Student VALUES (?, ?, ?, ?)");) {
            conn.setAutoCommit(false);
            pstmt.setInt(1, student.getStudentID());
            pstmt.setString(2, student.getName());
            pstmt.setString(3, student.getDepartment());
            pstmt.setDouble(4, student.getMarks());
            pstmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void viewStudents() {
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Student")) {
            System.out.println("StudentID | Name | Department | Marks");
            while (rs.next()) {
                System.out.println(rs.getInt("StudentID") + " | " + rs.getString("Name") + " | " +
                        rs.getString("Department") + " | " + rs.getDouble("Marks"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateStudent(int studentID, String name, String department, double marks) {
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement("UPDATE Student SET Name=?, Department=?, Marks=? WHERE StudentID=?")) {
            conn.setAutoCommit(false);
            pstmt.setString(1, name);
            pstmt.setString(2, department);
            pstmt.setDouble(3, marks);
            pstmt.setInt(4, studentID);
            pstmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteStudent(int studentID) {
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Student WHERE StudentID=?")) {
            conn.setAutoCommit(false);
            pstmt.setInt(1, studentID);
            pstmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

public class StudentManagementApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1. Add Student\n2. View Students\n3. Update Student\n4. Delete Student\n5. Exit");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.print("Enter StudentID, Name, Department, Marks: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    String name = scanner.nextLine();
                    String department = scanner.nextLine();
                    double marks = scanner.nextDouble();
                    DatabaseManager.addStudent(new Student(id, name, department, marks));
                    break;
                case 2:
                    DatabaseManager.viewStudents();
                    break;
                case 3:
                    System.out.print("Enter StudentID to update: ");
                    int updateID = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter new Name, Department, Marks: ");
                    String newName = scanner.nextLine();
                    String newDepartment = scanner.nextLine();
                    double newMarks = scanner.nextDouble();
                    DatabaseManager.updateStudent(updateID, newName, newDepartment, newMarks);
                    break;
                case 4:
                    System.out.print("Enter StudentID to delete: ");
                    int deleteID = scanner.nextInt();
                    DatabaseManager.deleteStudent(deleteID);
                    break;
                case 5:
                    System.out.println("Exiting...");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}

1. **Setup MySQL Database:**  
   - Install and start MySQL.  
   - Create a database (e.g., `StudentDB`).  
   - Create a table:  
     ```sql
     CREATE TABLE Student (
         StudentID INT PRIMARY KEY,
         Name VARCHAR(100),
         Department VARCHAR(50),
         Marks DOUBLE
     );
     ```

2. **Update Database Credentials:**  
   - Modify `URL`, `USER`, and `PASSWORD` in the code to match your MySQL database credentials.

3. **Add MySQL JDBC Driver:**  
   - Download and add `mysql-connector-java.jar` to your project's classpath.

4. **Compile and Run the Program:**  
   - Compile: `javac StudentManagementApp.java`  
   - Run: `java StudentManagementApp`  

5. **Menu-Driven Operations:**  
   - **Add Student:** Enter StudentID, Name, Department, and Marks.  
   - **View Students:** Displays all students in the table.  
   - **Update Student:** Modify Name, Department, or Marks using StudentID.  
   - **Delete Student:** Remove a student using StudentID.  
   - **Exit:** Quit the program.

6. **Transaction Handling:**  
   - Ensures data integrity by using `conn.setAutoCommit(false)` and `conn.commit()`.  
   - Rolls back changes in case of errors.

7. **Verify Database Changes:**  
   - Use `SELECT * FROM Student;` in MySQL to confirm modifications.
