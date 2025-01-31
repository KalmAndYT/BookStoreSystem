package entities;

public class Book {
    private int id;
    private String title;
    private String author;
    private String ISBN;
    private String category;
    private double price;
    private int stock;

    // Constructors
    public Book() {
        this.category = "Uncategorized"; // Default category
    }

    public Book(String title, String author, String isbn, String category, double price, int stock) {
        this.title = title != null ? title : "";
        this.author = author != null ? author : "";
        this.ISBN = isbn != null ? isbn : "";
        this.category = category != null ? category : "";
        this.price = price;
        this.stock = stock;
    }

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
        this.category = "Uncategorized"; // Default category
    }

    @Override
    public String toString() {
        return String.format("Title: %s, Author: %s, ISBN: %s, Category: %s, Price: %.2f, Stock: %d",
                title, author, ISBN, category, price, stock);
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getISBN() { return ISBN; }
    public void setISBN(String ISBN) { this.ISBN = ISBN; }

    public String getCategory() { return category; }
    public void setCategory(String category) {
        this.category = (category != null) ? category : "Uncategorized"; // Prevent null
    }

    public double getPrice() { return price; }
    public void setPrice(double price) {
        if (price < 0) throw new IllegalArgumentException("Price cannot be negative.");
        this.price = price;
    }

    public int getStock() { return stock; }
    public void setStock(int stock) {
        if (stock < 0) throw new IllegalArgumentException("Stock cannot be negative.");
        this.stock = stock;
    }
}