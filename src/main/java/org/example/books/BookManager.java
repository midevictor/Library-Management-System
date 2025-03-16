package org.example.books;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

public class BookManager {
    private BufferedWriter output;
    private  List<Book>  bookRecords = new ArrayList<>();
    private  Map<Integer, Book> bookMap = new HashMap<>();

    public void openFile() {
        try {
            output = new BufferedWriter(new FileWriter("books.txt", true));
        } catch (SecurityException securityException) {
            System.err.println("You do not have write access to this file");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Error creating file");
            System.exit(1);
        }
    }

    public void addBook(){
        Scanner input = new Scanner(System.in);
        boolean continueInput = true;

        while(continueInput){
            try {
                Book book = new Book();
                System.out.printf("%s\n%s", "Enter book id, title, author, genre, and available copies", "?");
                book.setBookId(input.nextInt());
                book.setTitle(input.next());
                book.setAuthor(input.next());
                book.setGenre(input.next());
                book.setAvailableCopies(input.nextInt());

                String recordString = String.format("%d %s %s %s %d",
                        book.getBookId(), book.getTitle(), book.getAuthor(), book.getGenre(), book.getAvailableCopies());
                output.write(recordString);
                output.newLine();
                bookRecords.add(book);
                bookMap.put(book.getBookId(), book);
                System.out.println("bookRecords: " + bookRecords);
                System.out.println("bookMap: " + bookMap);
                System.out.println("Record added successfully");

                System.out.print("Do you want to continue? (yes/no): ");
                String response = input.next();
                if (response.equalsIgnoreCase("no")) {
                    continueInput = false;
                }
            }  catch (FormatterClosedException formatterClosedException) {
                System.err.println("Error writing to file.");
                return;
            } catch (NoSuchElementException elementException) {
                System.err.println("Invalid input. Please try again");
                input.nextLine();
            } catch (IOException e) {
                System.err.println("Error writing to file");

            }
        }
    }

    public void closeFile() {
        try {
            if (output != null) {
                output.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readFile() {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter the file path to read from: ");
        String filePath = input.nextLine();

        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            String url = "jdbc:postgresql://ep-ancient-pond-abk7yv84-pooler.eu-west-2.aws.neon.tech/neondb?sslmode=require";
            String user = "neondb_owner";
            String password = "npg_icua3pobEN6q";
            try{
                Class.forName("org.postgresql.Driver");
                try(Connection connection = DriverManager.getConnection(url, user, password);
                    BufferedReader reader = new BufferedReader(new FileReader(file))){
                    bookRecords.clear();
                    bookMap.clear();
                    String line;
                    while((line = reader.readLine()) != null){
                        System.out.println(line);
                        String[] data = line.split(" ");
                        if(data.length == 5){
                            int book_id = Integer.parseInt(data[0]);
                            String title = data[1];
                            String author = data[2];
                            String genre = data[3];
                            int availableCopies = Integer.parseInt(data[4]);
                            Book book = new Book(book_id, title, author, genre, availableCopies);
                            bookRecords.add(book);
                            bookMap.put(book_id, book);
                            System.out.println("bookRecords: " + bookRecords);
                            System.out.println("bookMap: " + bookMap);

                            BookDAOImpl bookDAO = new BookDAOImpl(connection);
                            bookDAO.addBook(book);
                        }
                    }

                } catch (SQLException e) {
                    System.err.println("Error connecting to database: " + e.getMessage());
                } catch (IOException e) {
                    System.err.println("Error reading file");
                }
            } catch (ClassNotFoundException e) {
                System.err.println("Error loading PostgreSQL JDBC driver: " + e.getMessage());
            }

        }
    }

    public void exportToCSV(){

//        //loop through the bookMap and print out
//
        for(Map.Entry<Integer, Book> entry : bookMap.entrySet()){
            System.out.println(entry.getValue());
        }
//
//        for(Book book : bookRecords){
//            System.out.println(book);
//        }

//        try(BufferedWriter writer = new BufferedWriter(new FileWriter("books.csv"))){
//            writer.write("Book ID, Title, Author, Genre, Available Copies");
//            writer.newLine();
//            for(Book book : bookRecords){
//                writer.write(String.format("%d, %s, %s, %s, %d",
//                        book.getBookId(), book.getTitle(), book.getAuthor(), book.getGenre(), book.getAvailableCopies()));
//                writer.newLine();
//            }
//            System.out.println("Books exported to CSV successfully");
//        } catch(IOException e) {
//            System.err.println("Error exporting to CSV");
//        }
    }

    public void searchBooksByTitle(String title){
        for(Book book : bookRecords){
            if(book.getTitle().equalsIgnoreCase(title)){
                System.out.println(book);
            }
        }
    }

    public void searchBooksByAuthor(String author){
        System.out.println("bookRecords: " + bookRecords);
        System.out.println("bookMap: " + bookMap);
        for(Book book : bookRecords){
            if(book.getAuthor().equalsIgnoreCase(author)){
                System.out.println(book);
            }
        }
    }

    public void sortBooksByTitle(){
        bookRecords.sort(Comparator.comparing(Book::getTitle));
        for(Book book : bookRecords){
            System.out.println(book);
        }

    }


    public void sortBooksByGenre(){
        bookRecords.sort(Comparator.comparing(Book::getGenre));
        for(Book book : bookRecords){
            System.out.println(book);
        }
    }

    public void updateBook(int book_id, String title, String author, String genre, int available_copies){
        Book book = bookMap.get(book_id);
        if(book != null){
            book.setTitle(title);
            book.setAuthor(author);
            book.setGenre(genre);
            book.setAvailableCopies(available_copies);
            System.out.println("Book updated successfully");

            String url = "jdbc:postgresql://ep-ancient-pond-abk7yv84-pooler.eu-west-2.aws.neon.tech/neondb?sslmode=require";
            String user = "neondb_owner";
            String password = "npg_icua3pobEN6q";

            try(Connection connection = DriverManager.getConnection(url, user, password)){
                BookDAOImpl bookDAO = new BookDAOImpl(connection);
                bookDAO.updateBook(book);
            } catch (SQLException e) {
                System.err.println("Error connecting to database: " + e.getMessage());
            }
        } else {
            System.out.println("Book not found");
        }
    }

    public void deleteBook(int book_id){
        Book book = bookMap.get(book_id);
        if(book != null){
            bookRecords.remove(book);
            bookMap.remove(book_id);
            System.out.println("Book deleted successfully");

            String url = "jdbc:postgresql://ep-ancient-pond-abk7yv84-pooler.eu-west-2.aws.neon.tech/neondb?sslmode=require";
            String user = "neondb_owner";
            String password = "npg_icua3pobEN6q";

            try(Connection connection = DriverManager.getConnection(url, user, password)){
                BookDAOImpl bookDAO = new BookDAOImpl(connection);
                bookDAO.deleteBook(book_id);
            } catch (SQLException e) {
                System.err.println("Error connecting to database: " + e.getMessage());
            }
        } else {
            System.out.println("Book not found");
        }
    }




    public static void  main(String[] args) {
        String url = "jdbc:postgresql://ep-ancient-pond-abk7yv84-pooler.eu-west-2.aws.neon.tech/neondb?sslmode=require";
        String user = "neondb_owner";
        String password = "npg_icua3pobEN6q";

        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;
        BookManager app = new BookManager();
        while(isRunning){
            System.out.println("Menu");
            System.out.println("1. Add book from the books.txt file");
            System.out.println("2. Get Book by ID");
            System.out.println("3. Update Book");
            System.out.println("4. Delete Book");
            System.out.println("5. Get all Books");
            System.out.println("6. Export books to CSV");
            System.out.println("7. Search books by title");
            System.out.println("8. Search books by author");
            System.out.println("9. Sort books by title");
            System.out.println("10. Sort books by genre");
            System.out.println("11. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice){
                case 1:
                    app.openFile();
                    app.addBook();
                    app.closeFile();
                    app.readFile();
                    break;
                case 2:
                    System.out.print("Enter the ID of the book to get: ");
                    int id = scanner.nextInt();
                    try(Connection connection = DriverManager.getConnection(url, user, password)){
                        BookDAOImpl bookDAO = new BookDAOImpl(connection);
                        Book book = bookDAO.getBookById(id);
                        if(book != null){
                            System.out.println("Book ID: " + book.getBookId());
                            System.out.println("Title: " + book.getTitle());
                            System.out.println("Author: " + book.getAuthor());
                            System.out.println("Genre: " + book.getGenre());
                            System.out.println("Available Copies: " + book.getAvailableCopies());
                        } else {
                            System.out.println("No book found with id: " + id);
                        }
                    } catch (SQLException e) {
                        System.err.println("Error connecting to database: " + e.getMessage());
                    }
                    break;
                case 3:
                    System.out.print("Enter the ID of the book to update: ");
                    int bookId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter new title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter new author: ");
                    String author = scanner.nextLine();
                    System.out.print("Enter new genre: ");
                    String genre = scanner.nextLine();
                    System.out.print("Enter new available copies: ");
                    int availableCopies = scanner.nextInt();
                    app.updateBook(bookId, title, author, genre, availableCopies);
                    break;
                case 4:
                    System.out.print("Enter the ID of the book to delete: ");
                    int book_id = scanner.nextInt();
                    app.deleteBook(book_id);
                    break;
                case 5:
                    try(Connection connection = DriverManager.getConnection(url, user, password)){
                        BookDAOImpl bookDAO = new BookDAOImpl(connection);
                        bookDAO.getAllBooks();
                    } catch (SQLException e) {
                        System.err.println("Error connecting to database: " + e.getMessage());
                    }
                    break;
                case 6:
                    app.exportToCSV();
                    break;
                case 7:

                    System.out.print("Enter the title to search: ");
                    String titleToSearch = scanner.nextLine();
                    app.searchBooksByTitle(titleToSearch);
                    break;
                case 8:
                    System.out.print("Enter the author to search: ");
                    String authorToSearch = scanner.nextLine();
                    app.searchBooksByAuthor(authorToSearch);
                    break;
                case 9:
                    app.sortBooksByTitle();
                    break;
                case 10:
                    app.sortBooksByGenre();
                    break;
                case 11:
                    isRunning = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again");
            }



        }
        scanner.close();


    }

}
