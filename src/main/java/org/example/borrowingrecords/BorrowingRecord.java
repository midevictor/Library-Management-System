package org.example.borrowingrecords;

import java.util.Date;

public class BorrowingRecord {
    private int record_id;
    private int book_id;
    private int member_id;
    private Date borrow_date;
    private Date return_date;

    public BorrowingRecord(int record_id, int book_id, int member_id, Date borrow_date) {
        this.record_id = record_id;
        this.book_id = book_id;
        this.member_id = member_id;
        this.borrow_date = borrow_date;
        this.return_date = null;
    }

    public int getRecordId() {
        return record_id;
    }
    public  void setRecordId(int record_id) {
        this.record_id = record_id;
    }
    public int getBookId() {
        return book_id;
    }
    public void setBookId(int book_id) {
        this.book_id = book_id;
    }
    public int getMemberId() {
        return member_id;
    }
    public void setMemberId(int member_id) {
        this.member_id = member_id;
    }
    public Date getBorrowDate() {
        return borrow_date;
    }
    public void setBorrowDate(Date borrow_date) {
        this.borrow_date = borrow_date;
    }
    public Date getReturnDate() {
        return return_date;
    }
    public void setReturnDate(Date return_date) {
        this.return_date = return_date;
    }


    @Override
    public String toString() {
        return "Record ID: " + record_id + ", Book ID: " + book_id + ", Member ID: " + member_id +
                ", Borrow Date: " + borrow_date + ", Return Date: " + (return_date != null ? return_date : "Not returned");
    }

}
