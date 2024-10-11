package com.trungnguyen.bookservice.command.event;

import lombok.Data;

@Data
public class BookCreatedEvent {

    private String bookId;

    private String name;
    private String author;
    private Boolean isReady;

}
