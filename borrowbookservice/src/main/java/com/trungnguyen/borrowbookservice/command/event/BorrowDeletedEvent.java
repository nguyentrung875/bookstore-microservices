package com.trungnguyen.borrowbookservice.command.event;

import lombok.Data;

@Data
public class BorrowDeletedEvent {
    private String id;
}
