package entities;

import database.DatabaseHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class User extends Person {
    private int userId;
    private String role;
    private String email; // Новое поле

    // Геттеры и сеттеры
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void viewDetails() {
        System.out.println("User: " + getName() + " | Role: " + role + " | Email: " + email);
    }

    public boolean createUser(DatabaseHandler databaseHandler) {
        String query = "INSERT INTO users (user_id, name, email, role) VALUES (?, ?, ?, ?)";

        try (Connection connection = databaseHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            statement.setString(2, getName());
            statement.setString(3, email); // Вставляем email
            statement.setString(4, role);

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
            return false;
        }
    }
}
