package managers;

import database.DatabaseHandler;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InventoryManagerDB {
    private DatabaseHandler databaseHandler;

    public InventoryManagerDB(DatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
    }

    public void reduceStockInDB(String isbn, int quantity) {
        String sql = "UPDATE books SET stock = stock - ? WHERE isbn = ? AND stock >= ?";

        try (Connection connection = databaseHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, quantity);
            statement.setString(2, isbn);
            statement.setInt(3, quantity);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new IllegalArgumentException("Not enough stock available or invalid ISBN.");
            }
            System.out.println("Stock updated successfully for ISBN: " + isbn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void increaseStockInDB(String isbn, int quantity) {
        String sql = "UPDATE books SET stock = stock + ? WHERE isbn = ?";

        try (Connection connection = databaseHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, quantity);
            statement.setString(2, isbn);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new IllegalArgumentException("Invalid ISBN.");
            }
            System.out.println("Stock increased successfully for ISBN: " + isbn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
