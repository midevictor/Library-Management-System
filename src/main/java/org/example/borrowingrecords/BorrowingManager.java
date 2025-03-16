package org.example.borrowingrecords;

import org.example.books.BookDAOImpl;
import org.example.books.Book;
import org.example.members.Member;
import org.example.members.MemberDAOImpl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;


public class BorrowingManager {
    private final List<BorrowingRecord> borrowingRecords = new ArrayList<>();
    private final Map<Integer, Integer> borrowedBooks = new HashMap<>();
    private BufferedWriter logWriter;

    public BorrowingManager(){
        try {
            logWriter = new BufferedWriter(new FileWriter("borrowing_records.txt", true));
            loadBorrowingData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void loadBorrowingData() {
        String url = "jdbc:postgresql://ep-ancient-pond-abk7yv84-pooler.eu-west-2.aws.neon.tech/neondb?sslmode=require";
        String user = "neondb_owner";
        String password = "npg_icua3pobEN6q";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            BorrowingRecordDAOImpl borrowingRecordDAO = new BorrowingRecordDAOImpl(connection);
            List<BorrowingRecord> records = borrowingRecordDAO.getAllBorrowingRecords();

            for (BorrowingRecord record : records) {
                borrowingRecords.add(record);
                if (record.getReturnDate() == null) {
                    borrowedBooks.put(record.getBookId(), record.getMemberId());
                }
            }

            System.out.println("Loaded borrowing records: " + borrowingRecords);
            System.out.println("Loaded borrowed books map: " + borrowedBooks);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
//    public void borrowBook(int record_id ,int book_id, int member_id){
//        String url = "jdbc:postgresql://ep-ancient-pond-abk7yv84-pooler.eu-west-2.aws.neon.tech/neondb?sslmode=require";
//        String user = "neondb_owner";
//        String password = "npg_icua3pobEN6q";
//
//        try(Connection connection = DriverManager.getConnection(url, user, password)){
//            BookDAOImpl bookDAO = new BookDAOImpl(connection);
////            MemberDAOImpl memberDAO = new MemberDAOImpl(connection);
//            Book book = bookDAO.getBookById(book_id);
////            Member member = memberDAO.getMemberById(member_id);
//
//            if(book != null && book.getAvailableCopies() > 0){
//                System.out.println("Available Copies before borrowing: " + book.getAvailableCopies());
//                book.setAvailableCopies(book.getAvailableCopies() - 1);
//                bookDAO.updateBook(book);
//
//                BorrowingRecord record = new BorrowingRecord(record_id, book_id, member_id, new Date());
//                borrowingRecords.add(record);
//                borrowedBooks.put(book_id, member_id);
//
//                BorrowingRecordDAOImpl borrowingRecordDAO = new BorrowingRecordDAOImpl(connection);
//                borrowingRecordDAO.addBorrowingRecord(record);
//
//                logActivity("Borrowed book with ID: " + book_id + " by member with ID: " + member_id);
//            } else{
//                logActivity("Book with ID: " + book_id + " is not available for borrowing");
//            }
//
//        }catch(SQLException e){
//            System.err.println("Error connecting to database: " + e.getMessage());
//        }
//    }
public void borrowBook(int record_id, int book_id, int member_id) {
    String url = "jdbc:postgresql://ep-ancient-pond-abk7yv84-pooler.eu-west-2.aws.neon.tech/neondb?sslmode=require";
    String user = "neondb_owner";
    String password = "npg_icua3pobEN6q";

    try (Connection connection = DriverManager.getConnection(url, user, password)) {
        BookDAOImpl bookDAO = new BookDAOImpl(connection);
        MemberDAOImpl memberDAO = new MemberDAOImpl(connection);

        System.out.println("Fetching book with ID: " + book_id);
        Book book = bookDAO.getBookById(book_id);

        System.out.println("Fetching member with ID: " + member_id);
        Member member = memberDAO.getMemberById(member_id);

        if (book != null && member != null) {
            System.out.println("Available Copies before borrowing: " + book.getAvailableCopies());
            if (book.getAvailableCopies() > 0) {
                book.setAvailableCopies(book.getAvailableCopies() - 1);
                bookDAO.updateBook(book);
                System.out.println("Available Copies after borrowing: " + book.getAvailableCopies());

                BorrowingRecord record = new BorrowingRecord(record_id, book_id, member_id, new Date());
                BorrowingRecordDAOImpl borrowingRecordDAO = new BorrowingRecordDAOImpl(connection);
                borrowingRecordDAO.addBorrowingRecord(record);

                borrowedBooks.put(book_id, member_id);
                borrowingRecords.add(record);

                System.out.println("Borrowing record added: " + record);
                System.out.println("Current borrowedBooks map: " + borrowedBooks);
                System.out.println("Current borrowingRecords list: " + borrowingRecords);
                logActivity("Borrowed book with ID: " + book_id + " by member with ID: " + member_id);
            } else {
                System.out.println("Book with ID: " + book_id + " is not available for borrowing");
                logActivity("Book with ID: " + book_id + " is not available for borrowing");
            }
        } else {
            if (book == null) {
                System.out.println("Book with ID: " + book_id + " not found");
                logActivity("Book with ID: " + book_id + " not found");
            }
            if (member == null) {
                System.out.println("Member with ID: " + member_id + " not found");
                logActivity("Member with ID: " + member_id + " not found");
            }
        }

    } catch (SQLException e) {
        System.err.println("Error connecting to database: " + e.getMessage());
    }
}

    public void returnBook(int book_id, int member_id){
        String url = "jdbc:postgresql://ep-ancient-pond-abk7yv84-pooler.eu-west-2.aws.neon.tech/neondb?sslmode=require";
        String user = "neondb_owner";
        String password = "npg_icua3pobEN6q";

        try (Connection connection = DriverManager.getConnection(url, user, password)){
            BookDAOImpl bookDAO = new BookDAOImpl(connection);
            Book book = bookDAO.getBookById(book_id);


            System.out.println("Book fetched: " + book);
            System.out.println("Current borrowedBooks map: " + borrowedBooks);
            System.out.println("Current borrowingRecords list: " + borrowingRecords);

            if(book != null && borrowedBooks.containsKey(book_id) && borrowedBooks.get(book_id) == member_id){
                System.out.println("Book is borrowed by the member. Proceeding with return.");

                book.setAvailableCopies(book.getAvailableCopies() + 1);
                bookDAO.updateBook(book);

                for (BorrowingRecord record : borrowingRecords){
                    if(record.getBookId() == book_id && record.getMemberId() == member_id && record.getReturnDate() == null){
                        record.setReturnDate(new Date());
                        BorrowingRecordDAOImpl borrowingRecordDAO = new BorrowingRecordDAOImpl(connection);
                        borrowingRecordDAO.updateBorrowingRecord(record);
                        System.out.println("Borrowing record updated: " + record);
                        break;
                    }
                }
                borrowedBooks.remove(book_id);
                logActivity("Book returned: Book ID = " + book_id + ", Member ID = " + member_id);
            } else{
                System.out.println("Failed to return book: Book ID = " + book_id + " was not borrowed by Member ID = " + member_id);
                logActivity("Failed to return book: Book ID =" + book_id + " was not borrowed by Member ID = " + member_id);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void displayAllBorrowingRecords(){
        String url = "jdbc:postgresql://ep-ancient-pond-abk7yv84-pooler.eu-west-2.aws.neon.tech/neondb?sslmode=require";
        String user = "neondb_owner";
        String password = "npg_icua3pobEN6q";

        try(Connection connection = DriverManager.getConnection(url, user, password)){
            BorrowingRecordDAOImpl borrowingRecordDAO = new BorrowingRecordDAOImpl(connection);
            List<BorrowingRecord> records = borrowingRecordDAO.getAllBorrowingRecords();

            for(BorrowingRecord record : records){
                System.out.println("Record ID: " + record.getRecordId());
                System.out.println("Book ID: " + record.getBookId());
                System.out.println("Member ID: " + record.getMemberId());
                System.out.println("Borrow Date: " + record.getBorrowDate());
                System.out.println("Return Date: " + (record.getReturnDate() != null ? record.getReturnDate() : "Not returned"));
                System.out.println();
            }

        } catch (SQLException e){
            e.printStackTrace();
        }

    }

    private void logActivity(String activity){
        try {
            logWriter.write(new Date() + ":" + activity);
            logWriter.newLine();
            logWriter.flush();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void closeLog() {
        try {
            if(logWriter != null) {
                logWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  static void main (String[] args){
        String url = "jdbc:postgresql://ep-ancient-pond-abk7yv84-pooler.eu-west-2.aws.neon.tech/neondb?sslmode=require";
        String user = "neondb_owner";
        String password = "npg_icua3pobEN6q";

        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;
        BorrowingManager app = new BorrowingManager();

        while(isRunning){
            System.out.println("Menu");
            System.out.println("1. Borrow a book");
            System.out.println("2. Return a book");
            System.out.println("3. Display all borrowing records");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice){
                case 1:
                    System.out.println("Enter the record ID:");
                    int record_id = scanner.nextInt();
                    System.out.print("Enter the ID of the book to borrow: ");
                    int book_id = scanner.nextInt();
                    System.out.print("Enter the ID of the member borrowing the book: ");
                    int member_id = scanner.nextInt();
                    app.borrowBook(record_id, book_id, member_id);
                    break;
                case 2:
                    System.out.print("Enter the ID of the book to return: ");
                     int  bookreturn_id = scanner.nextInt();
                    System.out.print("Enter the ID of the member returning the book: ");
                    int memberreturn_id = scanner.nextInt();
                    app.returnBook(bookreturn_id, memberreturn_id);
                    break;
                case 3:
                    app.displayAllBorrowingRecords();
                    break;
                case 4:
                    isRunning = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
        app.closeLog();



    }
}
//
//package org.example.borrowingrecords;
//
//import org.example.books.BookDAOImpl;
//import org.example.books.Book;
//import org.example.members.Member;
//import org.example.members.MemberDAOImpl;
//
//import java.io.BufferedWriter;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.util.*;
//
//public class BorrowingManager {
//    private final List<BorrowingRecord> borrowingRecords = new ArrayList<>();
//    private final Map<Integer, Integer> borrowedBooks = new HashMap<>();
//    private BufferedWriter logWriter;
//
//    public BorrowingManager() {
//        try {
//            logWriter = new BufferedWriter(new FileWriter("borrowing_records.txt", true));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void borrowBook(int book_id, int member_id) {
//        String url = "jdbc:postgresql://ep-ancient-pond-abk7yv84-pooler.eu-west-2.aws.neon.tech/neondb?sslmode=require";
//        String user = "neondb_owner";
//        String password = "npg_icua3pobEN6q";
//
//        try (Connection connection = DriverManager.getConnection(url, user, password)) {
//            BookDAOImpl bookDAO = new BookDAOImpl(connection);
//            MemberDAOImpl memberDAO = new MemberDAOImpl(connection);
//            Book book = bookDAO.getBookById(book_id);
//            Member member = memberDAO.getMemberById(member_id);
//
//            if (book != null && book.getAvailableCopies() > 0) {
//                book.setAvailableCopies(book.getAvailableCopies() - 1);
//                bookDAO.updateBook(book);
//
//                BorrowingRecord record = new BorrowingRecord(book_id, member_id, new Date());
//                borrowingRecords.add(record);
//                borrowedBooks.put(book_id, member_id);
//
//                BorrowingRecordDAOImpl borrowingRecordDAO = new BorrowingRecordDAOImpl(connection);
//                borrowingRecordDAO.addBorrowingRecord(record);
//
//                logActivity("Borrowed book with ID: " + book_id + " by member with ID: " + member_id);
//            } else {
//                logActivity("Book with ID: " + book_id + " is not available for borrowing");
//            }
//
//        } catch (SQLException e) {
//            System.err.println("Error connecting to database: " + e.getMessage());
//        }
//    }
//
//    public void returnBook(int book_id, int member_id) {
//        String url = "jdbc:postgresql://ep-ancient-pond-abk7yv84-pooler.eu-west-2.aws.neon.tech/neondb?sslmode=require";
//        String user = "neondb_owner";
//        String password = "npg_icua3pobEN6q";
//
//        try (Connection connection = DriverManager.getConnection(url, user, password)) {
//            BookDAOImpl bookDAO = new BookDAOImpl(connection);
//            Book book = bookDAO.getBookById(book_id);
//
//            if (book != null && borrowedBooks.containsKey(book_id) && borrowedBooks.get(book_id) == member_id) {
//                book.setAvailableCopies(book.getAvailableCopies() + 1);
//                bookDAO.updateBook(book);
//
//                for (BorrowingRecord record : borrowingRecords) {
//                    if (record.getBookId() == book_id && record.getMemberId() == member_id && record.getReturnDate() == null) {
//                        record.setReturnDate(new Date());
//                        BorrowingRecordDAOImpl borrowingRecordDAO = new BorrowingRecordDAOImpl(connection);
//                        borrowingRecordDAO.updateBorrowingRecord(record);
//                        break;
//                    }
//                }
//                borrowedBooks.remove(book_id);
//                logActivity("Book returned: Book ID = " + book_id + ", Member ID = " + member_id);
//            } else {
//                logActivity("Failed to return book: Book ID = " + book_id + " was not borrowed by Member ID = " + member_id);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void displayAllBorrowingRecords() {
//        String url = "jdbc:postgresql://ep-ancient-pond-abk7yv84-pooler.eu-west-2.aws.neon.tech/neondb?sslmode=require";
//        String user = "neondb_owner";
//        String password = "npg_icua3pobEN6q";
//
//        try (Connection connection = DriverManager.getConnection(url, user, password)) {
//            BorrowingRecordDAOImpl borrowingRecordDAO = new BorrowingRecordDAOImpl(connection);
//            List<BorrowingRecord> records = borrowingRecordDAO.getAllBorrowingRecords();
//
//            for (BorrowingRecord record : records) {
////                System.out.println("Record ID: " + record.getRecordId());
//                System.out.println("Book ID: " + record.getBookId());
//                System.out.println("Member ID: " + record.getMemberId());
//                System.out.println("Borrow Date: " + record.getBorrowDate());
//                System.out.println("Return Date: " + (record.getReturnDate() != null ? record.getReturnDate() : "Not returned"));
//                System.out.println();
//            }
//
//        } catch (SQLException e) {
//            System.out.println("nothing dey here eje");
//        }
//    }
//
//    private void logActivity(String activity) {
//        try {
//            logWriter.write(new Date() + ":" + activity);
//            logWriter.newLine();
//            logWriter.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void closeLog() {
//        try {
//            if (logWriter != null) {
//                logWriter.close();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        boolean isRunning = true;
//        BorrowingManager app = new BorrowingManager();
//
//        while (isRunning) {
//            System.out.println("Menu");
//            System.out.println("1. Borrow a book");
//            System.out.println("2. Return a book");
//            System.out.println("3. Display all borrowing records");
//            System.out.println("4. Exit");
//            System.out.print("Enter your choice: ");
//            int choice = scanner.nextInt();
//            scanner.nextLine();
//
//            switch (choice) {
//                case 1:
//                    System.out.print("Enter the ID of the book to borrow: ");
//                    int book_id = scanner.nextInt();
//                    System.out.print("Enter the ID of the member borrowing the book: ");
//                    int member_id = scanner.nextInt();
//                    app.borrowBook(book_id, member_id);
//                    break;
//                case 2:
//                    System.out.print("Enter the ID of the book to return: ");
//                    int bookreturn_id = scanner.nextInt();
//                    System.out.print("Enter the ID of the member returning the book: ");
//                    int memberreturn_id = scanner.nextInt();
//                    app.returnBook(bookreturn_id, memberreturn_id);
//                    break;
//                case 3:
//                    app.displayAllBorrowingRecords();
//                    break;
//                case 4:
//                    isRunning = false;
//                    break;
//                default:
//                    System.out.println("Invalid choice. Please try again.");
//            }
//        }
//        scanner.close();
//        app.closeLog();
//    }
//}