import java.sql.*;
import java.util.Scanner;

public class EmployeeDB {
    static final String URL = "jdbc:mysql://localhost:3306/company";
    static final String USER = "root";
    static final String PASS = "password";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            while (true) {
                System.out.println("\n1. Add Employee\n2. View Employees\n3. Update Employee\n4. Delete Employee\n5. Exit");
                System.out.print("Choose: ");
                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1:
                        System.out.print("Name: ");
                        String name = sc.nextLine();
                        System.out.print("Position: ");
                        String position = sc.nextLine();
                        System.out.print("Salary: ");
                        double salary = sc.nextDouble();
                        addEmployee(conn, name, position, salary);
                        break;
                    case 2:
                        viewEmployees(conn);
                        break;
                    case 3:
                        System.out.print("ID to update: ");
                        int idToUpdate = sc.nextInt();
                        sc.nextLine();
                        System.out.print("New Name: ");
                        String newName = sc.nextLine();
                        System.out.print("New Position: ");
                        String newPosition = sc.nextLine();
                        System.out.print("New Salary: ");
                        double newSalary = sc.nextDouble();
                        updateEmployee(conn, idToUpdate, newName, newPosition, newSalary);
                        break;
                    case 4:
                        System.out.print("ID to delete: ");
                        int idToDelete = sc.nextInt();
                        deleteEmployee(conn, idToDelete);
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("Invalid choice.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void addEmployee(Connection conn, String name, String position, double salary) throws SQLException {
        String sql = "INSERT INTO employees (name, position, salary) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, position);
            stmt.setDouble(3, salary);
            stmt.executeUpdate();
            System.out.println("Employee added.");
        }
    }

    static void viewEmployees(Connection conn) throws SQLException {
        String sql = "SELECT * FROM employees";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            System.out.println("ID | Name | Position | Salary");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + " | " +
                                   rs.getString("name") + " | " +
                                   rs.getString("position") + " | " +
                                   rs.getDouble("salary"));
            }
        }
    }

    static void updateEmployee(Connection conn, int id, String name, String position, double salary) throws SQLException {
        String sql = "UPDATE employees SET name = ?, position = ?, salary = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, position);
            stmt.setDouble(3, salary);
            stmt.setInt(4, id);
            int rows = stmt.executeUpdate();
            System.out.println(rows > 0 ? "Employee updated." : "Employee not found.");
        }
    }

    static void deleteEmployee(Connection conn, int id) throws SQLException {
        String sql = "DELETE FROM employees WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            System.out.println(rows > 0 ? "Employee deleted." : "Employee not found.");
        }
    }
}