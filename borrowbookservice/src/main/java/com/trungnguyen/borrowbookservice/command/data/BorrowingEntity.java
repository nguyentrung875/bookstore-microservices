package com.trungnguyen.borrowbookservice.command.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity (name = "brrowing")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorrowingEntity {
    @Id
    private String id;

    private String bookId;

    private String employeeId;
    private Date borrowingDate;
    private Date returnDate;
}
