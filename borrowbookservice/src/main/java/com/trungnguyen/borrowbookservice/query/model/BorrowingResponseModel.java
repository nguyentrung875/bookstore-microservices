package com.trungnguyen.borrowbookservice.query.model;

import lombok.Data;

import java.util.Date;

@Data
public class BorrowingResponseModel {
    private String id;
    private String bookId;
    private String employeeId;
    private Date borrowingDate;
    private Date returnDate;
    private String nameBook;
    private String nameEmployee;

}
