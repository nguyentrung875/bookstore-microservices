package com.trungnguyen.borrowbookservice.command.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBorrowCommand {

    @TargetAggregateIdentifier
    private String id;

    private String bookId;
    private String employeeId;
    private Date borrowingDate;


}
