package main;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Database {

    public static void createNewTable() {
        // SQLite connection string
        String url = "jdbc:sqlite:C://sqlite/db/tiobe.db";

        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS users (\n"
                + "	id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + "	name VARCHAR(255) NOT NULL,\n"
                + "	surname VARCHAR(255) NOT NULL,\n"
                + "	email VARCHAR(255) NOT NULL,\n"
                + "	password VARCHAR(255) NOT NULL\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        // SQL statement for creating a new table
        sql = "CREATE TABLE IF NOT EXISTS billing_statements (\n"
                + "	id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + "	user_id INT NOT NULL,\n"
                + "	statement_date DATE NOT NULL,\n"
                + "	amount DECIMAL(10,2) NOT NULL,\n"
                + "	electric_amount DECIMAL(10,2) NOT NULL,\n"
                + "	address VARCHAR(255) NOT NULL\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void deleteUsersTable() {
        String url = "jdbc:sqlite:C://sqlite/db/tiobe.db";  // Replace with your database URL

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            String sql = "DROP TABLE IF EXISTS users";
            stmt.executeUpdate(sql);

            System.out.println("Users table deleted successfully.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void deleteBillingStatementsTable() {
        String url = "jdbc:sqlite:C://sqlite/db/tiobe.db";  // Replace with your database URL

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            String sql = "DROP TABLE IF EXISTS billing_statements";
            stmt.executeUpdate(sql);

            System.out.println("Billing Statements table deleted successfully.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:C://sqlite/db/tiobe.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    private static String encryptPass(String password){
        String encryptedpassword = null;
        try
        {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(password.getBytes());
            byte[] bytes = m.digest();
            StringBuilder s = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                s.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            encryptedpassword = s.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return encryptedpassword;
    }

    public static void registerUser(String name, String surname, String email, String password) {
        String sql = "INSERT INTO users(name,surname,email,password) VALUES(?,?,?,?)";
        password = encryptPass(password);
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, surname);
            pstmt.setString(3, email);
            pstmt.setString(4, password);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void registerBill(int userId, Date date, double amount, double electric_kW, String address) {
        String sql = "INSERT INTO billing_statements(user_id,statement_date,amount,electric_amount,address) VALUES(?,?,?,?,?)";
        long milliseconds = date.getTime();
        java.sql.Date sqlDate = new java.sql.Date(milliseconds);
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setDate(2, sqlDate);
            pstmt.setDouble(3, amount);
            pstmt.setDouble(4, electric_kW);
            pstmt.setString(5,address);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static boolean loginUser(String email, String password, UserData userData) {
        String sql = "SELECT id, name, surname FROM users WHERE email = ? AND password = ?";
        password = encryptPass(password);
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                // User login successful
                userData.id = rs.getInt("id");
                userData.name = rs.getString("name");
                userData.surname = rs.getString("surname");
                userData.email = email;
                return true;
            } else {
                // User login failed
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static List<Factura> getBillingStatements(int userId) {
        List<Factura> facturaList = new ArrayList<>();
        String name = "NULL";

        String sql = "SELECT bs.id, bs.statement_date, bs.amount, bs.electric_amount, bs.address, u.name, u.surname " +
                "FROM billing_statements bs " +
                "JOIN users u ON bs.user_id = u.id " +
                "WHERE bs.user_id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                name = rs.getString("name") + " " + rs.getString("surname");

                do {
                    int statementId = rs.getInt("id");
                    Date statementDate = rs.getDate("statement_date");
                    double amount = rs.getDouble("amount");
                    double electricAmount = rs.getDouble("electric_amount");
                    String address = rs.getString("address");

                    Factura factura = new Factura(userId, name, address, electricAmount, amount, statementDate);
                    facturaList.add(factura);
                } while (rs.next());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return facturaList;
    }

    public static void printAllUsers(){
        String sql = "SELECT id, name, surname, email, password FROM users";

        try (Connection conn = connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("id") +  "\t" +
                        rs.getString("name") + "\t" +
                        rs.getString("surname") + "\t" +
                        rs.getString("email") + "\t" +
                        rs.getString("password"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void printAllBillingStatements() {
        String sql = "SELECT id, user_id, statement_date, amount, electric_amount, address FROM billing_statements";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("id") + "\t" +
                        rs.getInt("user_id") + "\t" +
                        rs.getDate("statement_date") + "\t" +
                        rs.getDouble("amount") + "\t" +
                        rs.getDouble("electric_amount") + "\t" +
                        rs.getString("address"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
