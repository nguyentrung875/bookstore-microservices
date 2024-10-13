package com.trungnguyen.commonservice.model;

import lombok.Data;

import java.util.Date;

@Data
public class BorrowingResponseCommonModel {
    private String id;
    private String bookId;
    private String employeeId;
    private Date borrowingDate;
    private Date returnDate;
}
