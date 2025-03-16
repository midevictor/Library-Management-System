package org.example.borrowingrecords;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BorrowingRecordDAOImpl implements BorrowingRecordDAO {
    private final Connection connection;

    public BorrowingRecordDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addBorrowingRecord(BorrowingRecord record){
        String SQL = "INSERT INTO borrowing_records (record_id, book_id, member_id, borrow_date, return_date) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(SQL)) {
            ps.setInt(1, record.getRecordId());
            ps.setInt(2, record.getBookId());
            ps.setInt(3, record.getMemberId());
            ps.setDate(4, new java.sql.Date(record.getBorrowDate().getTime()));
//            ps.setDate(5, new java.sql.Date(record.getReturnDate().getTime()));
            if (record.getReturnDate() != null) {
                ps.setDate(5, new java.sql.Date(record.getReturnDate().getTime()));
            } else {
                ps.setNull(5, java.sql.Types.DATE);
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateBorrowingRecord(BorrowingRecord record){
        String SQL = "UPDATE borrowing_records SET book_id = ?, member_id = ?, borrow_date = ?, return_date = ? WHERE record_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(SQL)) {
            ps.setInt(1, record.getBookId());
            ps.setInt(2, record.getMemberId());
            ps.setDate(3, new java.sql.Date(record.getBorrowDate().getTime()));
            ps.setDate(4, new java.sql.Date(record.getReturnDate().getTime()));
            ps.setInt(5, record.getRecordId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<BorrowingRecord> getAllBorrowingRecords(){
        String sql = "SELECT * FROM borrowing_records";
        List<BorrowingRecord> records = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                   int recordId = rs.getInt("record_id");
                   int bookId = rs.getInt("book_id");
                   int memberId = rs.getInt("member_id");
                   Date borrowDate = rs.getDate("borrow_date");
                   Date returnDate = rs.getDate("return_date");
                   BorrowingRecord record = new BorrowingRecord(recordId, bookId, memberId, borrowDate);
                   record.setReturnDate(returnDate);
                   records.add(record);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }

    @Override
    public BorrowingRecord getBorrowingRecordById(int record_id){
        String sql = "SELECT * FROM borrowing_records WHERE record_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, record_id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int bookId = rs.getInt("book_id");
                    int memberId = rs.getInt("member_id");
                    Date borrowDate = rs.getDate("borrow_date");
                    Date returnDate = rs.getDate("return_date");
                    BorrowingRecord record = new BorrowingRecord(record_id, bookId, memberId, borrowDate);
                    record.setReturnDate(returnDate);
                    return record;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

//
//package org.example.borrowingrecords;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//public class BorrowingRecordDAOImpl implements BorrowingRecordDAO {
//    private final Connection connection;
//
//    public BorrowingRecordDAOImpl(Connection connection) {
//        this.connection = connection;
//    }
//
//    @Override
//    public void addBorrowingRecord(BorrowingRecord record) {
//        String SQL = "INSERT INTO borrowing_records (book_id, member_id, borrow_date, return_date) VALUES (?, ?, ?, ?)";
//        try (PreparedStatement ps = connection.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
//            ps.setInt(1, record.getBookId());
//            ps.setInt(2, record.getMemberId());
//            ps.setDate(3, new java.sql.Date(record.getBorrowDate().getTime()));
//            if (record.getReturnDate() != null) {
//                ps.setDate(4, new java.sql.Date(record.getReturnDate().getTime()));
//            } else {
//                ps.setNull(4, java.sql.Types.DATE);
//            }
//            ps.executeUpdate();
//
////            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
////                if (generatedKeys.next()) {
////                    record.setRecordId(generatedKeys.getInt(1));
////                }
////            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void updateBorrowingRecord(BorrowingRecord record) {
//        String SQL = "UPDATE borrowing_records SET book_id = ?, member_id = ?, borrow_date = ?, return_date = ? WHERE record_id = ?";
//        try (PreparedStatement ps = connection.prepareStatement(SQL)) {
//            ps.setInt(1, record.getBookId());
//            ps.setInt(2, record.getMemberId());
//            ps.setDate(3, new java.sql.Date(record.getBorrowDate().getTime()));
//            ps.setDate(4, new java.sql.Date(record.getReturnDate().getTime()));
//            ps.setInt(5, record.getRecordId());
//            ps.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public List<BorrowingRecord> getAllBorrowingRecords() {
//        String sql = "SELECT * FROM borrowing_records";
//        List<BorrowingRecord> records = new ArrayList<>();
//        try (PreparedStatement ps = connection.prepareStatement(sql)) {
//            try (ResultSet rs = ps.executeQuery()) {
//                while (rs.next()) {
////                    int recordId = rs.getInt("record_id");
//                    int bookId = rs.getInt("book_id");
//                    int memberId = rs.getInt("member_id");
//                    Date borrowDate = rs.getDate("borrow_date");
//                    Date returnDate = rs.getDate("return_date");
//                    BorrowingRecord record = new BorrowingRecord(bookId, memberId, borrowDate);
////                    record.setRecordId(recordId);
//                    record.setReturnDate(returnDate);
//                    records.add(record);
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return records;
//    }
//
//    @Override
//    public BorrowingRecord getBorrowingRecordById(int id) {
//        String sql = "SELECT * FROM borrowing_records WHERE id = ?";
//        try (PreparedStatement ps = connection.prepareStatement(sql)) {
//            ps.setInt(1, id);
//            try (ResultSet rs = ps.executeQuery()) {
//                if (rs.next()) {
//                    int bookId = rs.getInt("book_id");
//                    int memberId = rs.getInt("member_id");
//                    Date borrowDate = rs.getDate("borrow_date");
//                    Date returnDate = rs.getDate("return_date");
//                    BorrowingRecord record = new BorrowingRecord(bookId, memberId, borrowDate);
////                    record.setRecordId(record_id);
//                    record.setReturnDate(returnDate);
//                    return record;
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//}