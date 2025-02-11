import database.DatabaseHandler;
import entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class MainApp {
    private static DatabaseHandler databaseHandler = new DatabaseHandler();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== Book Store System ===");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            String input = scanner.nextLine().trim();
            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice. Please enter a number (1-3).");
                continue;
            }

            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    register();
                    break;
                case 3:
                    System.out.println("Exiting the system. Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void login() {
        System.out.print("Enter your user ID: ");
        String input = scanner.nextLine().trim();
        int userId;
        try {
            userId = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid user ID. Please enter a number.");
            return;
        }
        User user = getUserFromDatabase(userId);
        if (user == null) {
            System.out.println("User not found. Please register first.");
            return;
        }
        if ("Admin".equalsIgnoreCase(user.getRole())) {
            System.out.print("Enter admin password: ");
            String password = scanner.nextLine();
            if ("KOlovorot1".equals(password)) {
                adminMenu();
            } else {
                System.out.println("Invalid password.");
            }
        } else {
            customerMenu(user);
        }
    }

    private static void register() {
        System.out.print("Enter user ID: ");
        String input = scanner.nextLine().trim();
        int userId;
        try {
            userId = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid user ID. Please enter a number.");
            return;
        }

        // Check if user ID already exists
        User existingUser = getUserFromDatabase(userId);
        if (existingUser != null) {
            System.out.println("User ID already exists. Registration failed.");
            return;
        }

        System.out.print("Enter user name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Name cannot be empty. Registration failed.");
            return;
        }

        System.out.print("Enter user email: ");
        String email = scanner.nextLine().trim();
        if (email.isEmpty()) {
            System.out.println("Email cannot be empty. Registration failed.");
            return;
        }

        // Check if email already exists in the database
        if (isEmailAlreadyExists(email)) {
            System.out.println("Email already exists. Registration failed.");
            return;
        }

        System.out.print("Enter user role (Admin/Customer): ");
        String role = scanner.nextLine().trim();

        // Validate role
        if (!("Admin".equalsIgnoreCase(role) || "Customer".equalsIgnoreCase(role))) {
            System.out.println("Invalid role. Please enter Admin or Customer.");
            return;
        }

        // Admin registration password check
        if ("Admin".equalsIgnoreCase(role)) {
            System.out.print("Enter admin registration password: ");
            String password = scanner.nextLine();
            if (!"KOlovorot1".equals(password)) {
                System.out.println("Invalid admin password. Registration failed.");
                return;
            }
        }

        // Insert new user into the database
        String query = "INSERT INTO users (user_id, name, email, role) VALUES (?, ?, ?, ?)";

        try (Connection connection = databaseHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            statement.setString(2, name);
            statement.setString(3, email);
            statement.setString(4, role);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("User registered successfully!");
            } else {
                System.out.println("Failed to register user.");
            }
        } catch (SQLException e) {
            System.err.println("Error registering user: " + e.getMessage());
        }
    }

    private static boolean isEmailAlreadyExists(String email) {
        String query = "SELECT * FROM users WHERE email = ?";

        try (Connection connection = databaseHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next(); // Returns true if email exists
        } catch (SQLException e) {
            System.err.println("Error checking email: " + e.getMessage());
            return false;
        }
    }

    // Меню администратора
    private static void adminMenu() {
        while (true) {
            System.out.println("\n=== Admin Menu ===");
            System.out.println("1. View all books");
            System.out.println("2. Add a new book");
            System.out.println("3. Delete a book");
            System.out.println("4. Update book details");
            System.out.println("5. Search book by ISBN");  // Новый пункт для поиска
            System.out.println("6. View statistics");
            System.out.println("7. Back to main menu");
            System.out.print("Enter your choice: ");

            String input = scanner.nextLine().trim();
            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice. Please enter a number (1-7).");
                continue;
            }

            switch (choice) {
                case 1:
                    viewAllBooks();
                    break;
                case 2:
                    addNewBook();
                    break;
                case 3:
                    deleteBook();
                    break;
                case 4:
                    updateBookDetails();
                    break;
                case 5:
                    searchBookByIsbn();  // Вызов функции поиска по ISBN
                    break;
                case 6:
                    viewStatistics();
                    break;
                case 7:
                    return; // Exit to main menu
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void updateBookDetails() {
        System.out.print("Enter the ISBN of the book to update: ");
        String isbn = scanner.nextLine();

        System.out.print("Enter new title (leave blank to keep unchanged): ");
        String title = scanner.nextLine().trim();
        System.out.print("Enter new author (leave blank to keep unchanged): ");
        String author = scanner.nextLine().trim();
        System.out.print("Enter new category (leave blank to keep unchanged): ");
        String category = scanner.nextLine().trim();
        System.out.print("Enter new price (-1 to keep unchanged): ");
        double price = scanner.nextDouble();
        System.out.print("Enter new stock (-1 to keep unchanged): ");
        int stock = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        String query = "UPDATE books SET title = COALESCE(NULLIF(?, ''), title), author = COALESCE(NULLIF(?, ''), author), " +
                "category = COALESCE(NULLIF(?, ''), category), price = COALESCE(NULLIF(?, -1), price), stock = COALESCE(NULLIF(?, -1), stock) " +
                "WHERE isbn = ?";

        try (Connection connection = databaseHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, title);
            statement.setString(2, author);
            statement.setString(3, category);
            statement.setDouble(4, price);
            statement.setInt(5, stock);
            statement.setString(6, isbn);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Book details updated successfully!");
            } else {
                System.out.println("No book found with the given ISBN.");
            }
        } catch (SQLException e) {
            System.err.println("Error updating book: " + e.getMessage());
        }
    }

    private static void viewStatistics() {
        String totalBooksQuery = "SELECT COUNT(*) AS total_books FROM books";
        String totalSalesQuery = "SELECT SUM(quantity) AS total_sales FROM transactions";
        String lowStockQuery = "SELECT COUNT(*) AS low_stock FROM books WHERE stock < 5";

        try (Connection connection = databaseHandler.getConnection()) {
            try (PreparedStatement totalBooksStmt = connection.prepareStatement(totalBooksQuery);
                 ResultSet booksResult = totalBooksStmt.executeQuery()) {
                if (booksResult.next()) {
                    System.out.println("Total Books in Store: " + booksResult.getInt("total_books"));
                }
            }

            try (PreparedStatement totalSalesStmt = connection.prepareStatement(totalSalesQuery);
                 ResultSet salesResult = totalSalesStmt.executeQuery()) {
                if (salesResult.next()) {
                    System.out.println("Total Books Sold: " + salesResult.getInt("total_sales"));
                }
            }

            try (PreparedStatement lowStockStmt = connection.prepareStatement(lowStockQuery);
                 ResultSet stockResult = lowStockStmt.executeQuery()) {
                if (stockResult.next()) {
                    System.out.println("Books with Low Stock (<5 units): " + stockResult.getInt("low_stock"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving statistics: " + e.getMessage());
        }
    }


    private static void deleteBook() {
        System.out.print("Enter the ISBN of the book to delete: ");
        String isbnToDelete = scanner.nextLine();

        String query = "DELETE FROM books WHERE isbn = ?";

        try (Connection connection = databaseHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, isbnToDelete);

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Book deleted successfully!");
            } else {
                System.out.println("No book found with the given ISBN.");
            }
        } catch (SQLException e) {
            System.err.println("Error deleting book: " + e.getMessage());
        }
    }


    private static void addNewBook() {
        System.out.print("Enter book title: ");
        String title = scanner.nextLine();

        System.out.print("Enter book author: ");
        String author = scanner.nextLine();

        System.out.print("Enter book ISBN: ");
        String isbn = scanner.nextLine();

        System.out.print("Enter book category: ");
        String category = scanner.nextLine();

        System.out.print("Enter book price: ");
        double price = scanner.nextDouble();

        System.out.print("Enter book stock: ");
        int stock = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        String query = "INSERT INTO books (isbn, title, author, category, price, stock) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = databaseHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, isbn);
            statement.setString(2, title);
            statement.setString(3, author);
            statement.setString(4, category);
            statement.setDouble(5, price);
            statement.setInt(6, stock);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Book added successfully!");
            } else {
                System.out.println("Failed to add book.");
            }
        } catch (SQLException e) {
            System.err.println("Error adding book: " + e.getMessage());
        }
    }

    // Меню покупателя
    private static void customerMenu(User customer) {
        while (true) {
            System.out.println("\n=== Customer Menu ===");
            System.out.println("1. View all books");
            System.out.println("2. Buy a book");
            System.out.println("3. Search book by ISBN");  // Новый пункт для поиска
            System.out.println("4. Back to main menu");
            System.out.print("Enter your choice: ");

            String input = scanner.nextLine().trim();
            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice. Please enter a number (1-4).");
                continue;
            }

            switch (choice) {
                case 1:
                    viewAllBooks();
                    break;
                case 2:
                    buyBook(customer);
                    break;
                case 3:
                    searchBookByIsbn();  // Вызов функции поиска по ISBN
                    break;
                case 4:
                    return; // Exit to main menu
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void searchBookByIsbn() {
        System.out.print("Enter the ISBN of the book to search: ");
        String isbn = scanner.nextLine().trim();

        String query = "SELECT * FROM books WHERE isbn = ?";
        try (Connection connection = databaseHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, isbn);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                System.out.printf("ISBN: %s, Title: %s, Author: %s, Category: %s, Price: %.2f, Stock: %d%n",
                        resultSet.getString("isbn"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("category"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("stock"));
            } else {
                System.out.println("Book not found.");
            }
        } catch (SQLException e) {
            System.err.println("Error searching for book: " + e.getMessage());
        }
    }

    private static void buyBook(User customer) {
        // Отобразить доступные книги
        System.out.println("\nAvailable books for purchase:");
        viewAllBooks();

        System.out.print("Enter the ISBN of the book you want to buy: ");
        String isbn = scanner.nextLine();

        System.out.print("Enter the quantity: ");
        String quantityInput = scanner.nextLine().trim();
        int quantity;
        try {
            quantity = Integer.parseInt(quantityInput);
        } catch (NumberFormatException e) {
            System.out.println("Invalid quantity. Please enter a number.");
            return;
        }

        // Проверка наличия книги
        String checkQuery = "SELECT * FROM books WHERE isbn = ?";
        try (Connection connection = databaseHandler.getConnection();
             PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {

            checkStatement.setString(1, isbn);
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next()) {
                int stock = resultSet.getInt("stock");
                int bookId = resultSet.getInt("id");

                if (stock >= quantity) {
                    // Обновление запасов
                    String updateQuery = "UPDATE books SET stock = stock - ? WHERE isbn = ?";
                    try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                        updateStatement.setInt(1, quantity);
                        updateStatement.setString(2, isbn);
                        updateStatement.executeUpdate();
                    }

                    // Сохранение транзакции
                    String transactionQuery = "INSERT INTO transactions (user_id, book_id, quantity) VALUES (?, ?, ?)";
                    try (PreparedStatement transactionStatement = connection.prepareStatement(transactionQuery)) {
                        transactionStatement.setInt(1, customer.getUserId());
                        transactionStatement.setInt(2, bookId);
                        transactionStatement.setInt(3, quantity);
                        transactionStatement.executeUpdate();
                    }

                    System.out.println("Purchase successful!");
                } else {
                    System.out.println("Insufficient stock. Available: " + stock);
                }
            } else {
                System.out.println("Book not found.");
            }
        } catch (SQLException e) {
            System.err.println("Error during purchase: " + e.getMessage());
        }
    }


    // Просмотр всех книг
    private static void viewAllBooks() {
        String query = "SELECT * FROM books";

        try (Connection connection = databaseHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            System.out.println("\n=== List of Books ===");
            while (resultSet.next()) {
                System.out.printf("ISBN: %s, Title: %s, Author: %s, Category: %s, Price: %.2f, Stock: %d%n",
                        resultSet.getString("isbn"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("category"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("stock"));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching books: " + e.getMessage());
        }
    }

    // Добавление нового пользователя
    private static void createNewUser() {
        System.out.print("Enter user ID: ");
        int userId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter user name: ");
        String name = scanner.nextLine();

        System.out.print("Enter user email: ");
        String email = scanner.nextLine();

        System.out.print("Enter user role (Admin/Customer): ");
        String role = scanner.nextLine();

        String query = "INSERT INTO users (user_id, name, email, role) VALUES (?, ?, ?, ?)";

        try (Connection connection = databaseHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            statement.setString(2, name);
            statement.setString(3, email);
            statement.setString(4, role);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("User created successfully!");
            } else {
                System.out.println("Failed to create user.");
            }
        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
        }
    }

    // Проверка, существует ли пользователь
    private static boolean userExists(int userId) {
        String query = "SELECT * FROM users WHERE user_id = ?";

        try (Connection connection = databaseHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();

        } catch (SQLException e) {
            System.err.println("Error checking user existence: " + e.getMessage());
            return false;
        }
    }

    // Получение пользователя из базы данных
    private static User getUserFromDatabase(int userId) {
        String query = "SELECT * FROM users WHERE user_id = ?";

        try (Connection connection = databaseHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                User user = new User();
                user.setUserId(userId);
                user.setName(resultSet.getString("name"));
                user.setEmail(resultSet.getString("email"));
                user.setRole(resultSet.getString("role"));
                return user;
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user: " + e.getMessage());
        }
        return null;
    }
}
