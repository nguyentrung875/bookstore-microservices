package com.trungnguyen.borrowbookservice.command.event;

import lombok.Data;

import java.util.Date;

@Data
public class BorrowingUpdateBookReturnEvent {

    private String id;
    private String bookId;
    private String employee;
    private Date returnDate;
}
