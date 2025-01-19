package API;
import java.sql.*;
import java.util.ArrayList;

import model.User;

public class DatabaseHandler {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/mmc";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    // Mengambil data user dari database
    public ArrayList<User> getUsers() {
        ArrayList<User> users = new ArrayList<>();
        String query = "SELECT * FROM user ORDER BY score DESC";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
    
            while (rs.next()) {
                int id = rs.getInt("id_user");
                String name = rs.getString("name");
                int score = rs.getInt("score");
    
                // Buat objek User dan tambahkan ke daftar
                User user = new User(id, name, score);
                users.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching users: " + e.getMessage());
        }
        return users;
    }
    

    // Update score atau tambah player baru
    public void updateScore(String playerName, int score) {
        String query = "INSERT INTO user (name, score) VALUES (?, ?) ON DUPLICATE KEY UPDATE score = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, playerName);
            stmt.setInt(2, score);
            stmt.setInt(3, score);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating score: " + e.getMessage());
        }
    }

    public boolean checkIfUserExists(String playerName) {
        String query = "SELECT * FROM user WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
    
            stmt.setString(1, playerName);
            ResultSet rs = stmt.executeQuery();
    
            // Jika ada hasil (user ditemukan), maka return true
            return rs.next(); // Jika data ditemukan, next() akan mengembalikan true
        } catch (SQLException e) {
            System.err.println("Error checking user existence: " + e.getMessage());
            return false;
        }
    }

    public int getScore(String playerName) {
        int score = 0;
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT score FROM user WHERE name = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, playerName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                score = resultSet.getInt("score");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return score;
    }
}