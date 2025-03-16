package org.example.books;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class BookDAOImpl implements BookDAO {
    private Connection connection;

    public BookDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addBook(Book book) {
        String SQL = "INSERT INTO book (book_id, title, author, genre, available_copies) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(SQL)) {
            ps.setInt(1, book.getBookId());
            ps.setString(2, book.getTitle());
            ps.setString(3, book.getAuthor());
            ps.setString(4, book.getGenre());
            ps.setInt(5, book.getAvailableCopies());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateBook(Book book) {
        String SQL = "UPDATE book SET title = ?, author = ?, genre = ?, available_copies = ? WHERE book_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(SQL)) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getGenre());
            ps.setInt(4, book.getAvailableCopies());
            ps.setInt(5, book.getBookId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteBook(int book_id) {
        String SQL = "DELETE FROM book WHERE book_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(SQL)) {
            ps.setInt(1, book_id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Book> getAllBooks() {
        String sql = "SELECT * FROM book";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    System.out.println("Book ID: " + rs.getInt("book_id"));
                    System.out.println("Title: " + rs.getString("title"));
                    System.out.println("Author: " + rs.getString("author"));
                    System.out.println("Genre: " + rs.getString("genre"));
                    System.out.println("Available Copies: " + rs.getInt("available_copies"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
//    public Book getBookById(int book_id) {
//        String sql = "SELECT * FROM book WHERE book_id = ?";
//        try (PreparedStatement ps = connection.prepareStatement(sql)) {
//            ps.setInt(1, book_id);
//            try (ResultSet rs = ps.executeQuery()) {
//                if (rs.next()) {
//                    System.out.println("Book ID: " + rs.getInt("book_id"));
//                    System.out.println("Title: " + rs.getString("title"));
//                    System.out.println("Author: " + rs.getString("author"));
//                    System.out.println("Genre: " + rs.getString("genre"));
//                    System.out.println("Available Copies: " + rs.getInt("available_copies"));
//                } else {
//                    System.out.println("No book record found with id: " + book_id);
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//
//        }
//        return null;
//    }

    public Book getBookById(int book_id) {
        String SQL = "SELECT * FROM book WHERE book_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(SQL)) {
            ps.setInt(1, book_id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Book book = new Book(
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("genre"),
                        rs.getInt("available_copies")
                );
                System.out.println("Book fetched: " + book);
                return book;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }




}
