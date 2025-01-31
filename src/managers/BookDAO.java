package managers;

import database.DatabaseHandler;
import entities.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    // Метод для получения всех книг
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM books";

        try (Connection connection = DatabaseHandler.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Book book = new Book(
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("ISBN"),
                        resultSet.getString("category"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("stock")
                );
                book.setId(resultSet.getInt("id")); // Устанавливаем ID, если он есть
                books.add(book);
            }
        } catch (SQLException e) {
            System.err.println("❌ ERROR: Failed to fetch books from the database!");
            e.printStackTrace();
        }

        return books;
    }

    // Добавление книги в БД
    public void addBook(Book book) {
        String query = "INSERT INTO books (title, author, ISBN, category, price, stock) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = DatabaseHandler.getInstance().getConnection().prepareStatement(query)) {
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getISBN());
            stmt.setString(4, book.getCategory());
            stmt.setDouble(5, book.getPrice());
            stmt.setInt(6, book.getStock());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Удаление книги из БД
    public void deleteBook(int bookId) {
        String query = "DELETE FROM books WHERE id = ?";
        try (PreparedStatement stmt = DatabaseHandler.getInstance().getConnection().prepareStatement(query)) {
            stmt.setInt(1, bookId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}