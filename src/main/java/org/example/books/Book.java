package org.example.books;

public class Book {
    private int book_id;
    private String title;
    private String author;
    private String genre;
    private int available_copies;

    public Book() {

    }

    public Book(int book_id, String title, String author, String genre, int available_copies) {
        this.book_id = book_id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.available_copies = available_copies;
    }

    public int getBookId() {
        return book_id;
    }
    public void  setBookId(int book_id) {
        this.book_id = book_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getAvailableCopies() {
        return available_copies;
    }

    public void setAvailableCopies(int available_copies) {
        this.available_copies = available_copies;
    }


    @Override
    public String toString() {
        return String.format("Book ID: %d, Title: %s, Author: %s, Genre: %s, Available Copies: %d",
                book_id, title, author, genre, available_copies);
    }
}
