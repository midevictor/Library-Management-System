package org.example.books;
import java.util.List;


public interface BookDAO {
    void addBook(Book book);
    Book getBookById(int book_id);
    List<Book> getAllBooks();
    void updateBook(Book book);
    void deleteBook(int book_id);
}
