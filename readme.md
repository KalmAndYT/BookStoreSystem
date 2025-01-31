Book Store System

Project Overview
This is a Java-based backend application for managing a book store. The system provides functionalities for user management, book inventory management, transactions, and an admin panel. The project demonstrates Object-Oriented Programming (OOP) principles and follows the SOLID design principles.

Features

1. User Management
- Users can register and log in.
- Users can have different roles: Admin or Customer.
- Admins have additional privileges.

2. Book Management
- Admins can add, update, delete, and view books.
- Books have attributes such as title, author, ISBN, category, price, and stock.

3. Transaction System
- Customers can view available books.
- Customers can purchase books, which updates inventory stock.
- Transactions are logged in the database.

4. Admin Panel
- View all books.
- Manage inventory.
- View statistics (total books, total sales, low-stock alerts).

Project Structure

bookstore-system
- src
  - database
    - DatabaseHandler.java
  - entities
    - Book.java
    - User.java
    - Person.java
    - Transaction.java
  - exceptions
    - InvalidISBNException.java
  - managers
    - BookDAO.java
    - BookManager.java
    - InventoryManagerDB.java
  - MainApp.java
- README.md
- database (SQL scripts and configurations)

OOP Principles Implemented
- Encapsulation: All fields are private with getter/setter methods.
- Inheritance: User extends Person.
- Polymorphism: User.viewDetails() overrides Person.viewDetails().
- Method Overloading: Multiple constructors in Book.
- Method Overriding: Admin and Customer have different views.
- Exception Handling: InvalidISBNException.java for input validation.

Database Integration
- Uses PostgreSQL as a DBMS.
- DatabaseHandler.java manages a singleton database connection.
- Transactions are logged with Transaction.java.

Installation & Setup

Prerequisites
- Java 11+
- PostgreSQL installed and running.
- JDBC Driver for PostgreSQL.

Database Setup
1. Create a database in PostgreSQL:
   CREATE DATABASE bookstore;

2. Create the required tables:
   CREATE TABLE users (
       user_id SERIAL PRIMARY KEY,
       name VARCHAR(255),
       email VARCHAR(255) UNIQUE,
       role VARCHAR(50)
   );
   CREATE TABLE books (
       id SERIAL PRIMARY KEY,
       title VARCHAR(255),
       author VARCHAR(255),
       ISBN VARCHAR(13) UNIQUE,
       category VARCHAR(100),
       price DECIMAL(10,2),
       stock INT
   );
   CREATE TABLE transactions (
       transaction_id SERIAL PRIMARY KEY,
       user_id INT REFERENCES users(user_id),
       book_id INT REFERENCES books(id),
       quantity INT,
       date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
   );

Running the Application
1. Clone the repository (if applicable):
   git clone <repository-url>
   cd bookstore-system

2. Compile and run:
   javac -d bin -cp . src/**/*.java
   java -cp bin MainApp

Contributors
- Bober Danial - Developer

This project is submitted as part of the Endterm Project for Object-Oriented Programming.

