package org.example.borrowingrecords;

import java.util.List;

public interface BorrowingRecordDAO {
    void addBorrowingRecord(BorrowingRecord record);
    BorrowingRecord getBorrowingRecordById(int record_id);
    List<BorrowingRecord> getAllBorrowingRecords();
    void updateBorrowingRecord(BorrowingRecord record);

}
