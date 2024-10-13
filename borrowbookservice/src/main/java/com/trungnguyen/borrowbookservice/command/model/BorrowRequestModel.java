package com.trungnguyen.borrowbookservice.command.model;

import lombok.Getter;

import java.util.Date;

@Getter
public class BorrowRequestModel {
    private String id;
    private String bookId;
    private String employeeId;
    private Date borrowingDate;
    private Date returnDate;

}
