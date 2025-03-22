### Instructions to Run the Java CRUD Program:
CODE:
import java.sql.*;
import java.util.Scanner;

class Product {
    private int productID;
    private String productName;
    private double price;
    private int quantity;

    public Product(int productID, String productName, double price, int quantity) {
        this.productID = productID;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    public int getProductID() { return productID; }
    public String getProductName() { return productName; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
}

class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/your_database";
    private static final String USER = "your_username";
    private static final String PASSWORD = "your_password";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void addProduct(Product product) {
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Product VALUES (?, ?, ?, ?)");) {
            conn.setAutoCommit(false);
            pstmt.setInt(1, product.getProductID());
            pstmt.setString(2, product.getProductName());
            pstmt.setDouble(3, product.getPrice());
            pstmt.setInt(4, product.getQuantity());
            pstmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void viewProducts() {
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Product")) {
            System.out.println("ProductID | ProductName | Price | Quantity");
            while (rs.next()) {
                System.out.println(rs.getInt("ProductID") + " | " + rs.getString("ProductName") + " | " +
                        rs.getDouble("Price") + " | " + rs.getInt("Quantity"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateProduct(int productID, String productName, double price, int quantity) {
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement("UPDATE Product SET ProductName=?, Price=?, Quantity=? WHERE ProductID=?")) {
            conn.setAutoCommit(false);
            pstmt.setString(1, productName);
            pstmt.setDouble(2, price);
            pstmt.setInt(3, quantity);
            pstmt.setInt(4, productID);
            pstmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteProduct(int productID) {
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Product WHERE ProductID=?")) {
            conn.setAutoCommit(false);
            pstmt.setInt(1, productID);
            pstmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

public class ProductCRUD {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1. Add Product\n2. View Products\n3. Update Product\n4. Delete Product\n5. Exit");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.print("Enter ProductID, ProductName, Price, Quantity: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    String name = scanner.nextLine();
                    double price = scanner.nextDouble();
                    int quantity = scanner.nextInt();
                    DatabaseManager.addProduct(new Product(id, name, price, quantity));
                    break;
                case 2:
                    DatabaseManager.viewProducts();
                    break;
                case 3:
                    System.out.print("Enter ProductID to update: ");
                    int updateID = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter new ProductName, Price, Quantity: ");
                    String newName = scanner.nextLine();
                    double newPrice = scanner.nextDouble();
                    int newQuantity = scanner.nextInt();
                    DatabaseManager.updateProduct(updateID, newName, newPrice, newQuantity);
                    break;
                case 4:
                    System.out.print("Enter ProductID to delete: ");
                    int deleteID = scanner.nextInt();
                    DatabaseManager.deleteProduct(deleteID);
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


1. **Setup MySQL Database**
   - Ensure MySQL is installed and running.
   - Create a database and a `Product` table with columns `ProductID`, `ProductName`, `Price`, and `Quantity`.

2. **Update Database Credentials**
   - Replace `your_database`, `your_username`, and `your_password` in the code with actual database credentials.

3. **Add MySQL JDBC Driver**
   - Download and add `mysql-connector-java.jar` to your project’s classpath.

4. **Compile and Run the Program**
   - Compile: `javac ProductCRUD.java`
   - Run: `java ProductCRUD`

5. **Menu-Driven Operations**
   - Select options to **Create**, **Read**, **Update**, or **Delete** products.
   - Input values as prompted.

6. **Transaction Handling**
   - Transactions ensure data integrity.
   - If an error occurs, changes are rolled back.

7. **Verify Output**
   - Ensure product records are correctly manipulated in the database.
