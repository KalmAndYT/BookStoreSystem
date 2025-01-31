package managers;

import entities.Book;
import exceptions.InvalidISBNException;

public class BookManager {
    private BookDAO bookDAO = new BookDAO();

    // Добавление книги с проверкой ISBN
    public void addBook(Book book) throws InvalidISBNException {
        if (book.getISBN().length() != 13) {
            throw new InvalidISBNException("ISBN must be 13 characters long.");
        }
        bookDAO.addBook(book);
    }

    // Удаление книги
    public void deleteBook(int bookId) {
        bookDAO.deleteBook(bookId);
    }
}