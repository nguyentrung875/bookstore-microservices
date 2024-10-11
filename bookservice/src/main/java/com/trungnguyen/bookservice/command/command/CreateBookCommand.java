package com.trungnguyen.bookservice.command.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookCommand {
    @TargetAggregateIdentifier //định nghĩa bookId này là Identify
    private String bookId;

    private String name;
    private String author;
    private Boolean isReady;
}
