package com.trungnguyen.bookservice.command.controller;

import com.trungnguyen.bookservice.command.command.CreateBookCommand;
import com.trungnguyen.bookservice.command.command.DeleteBookCommand;
import com.trungnguyen.bookservice.command.command.UpdateBookCommand;
import com.trungnguyen.bookservice.command.model.BookRequestModel;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/books")
public class BookCommandController {

    @Autowired
    private CommandGateway commandGateway;

    @PostMapping
    public String addBook(@RequestBody BookRequestModel model) {
        //Tạo command tạo một book
        CreateBookCommand command =
                new CreateBookCommand(UUID.randomUUID().toString(),model.getName(), model.getAuthor(), true);
        commandGateway.sendAndWait(command); //gửi command cho command Handler xử lý event
        return "added Book";
    }
    @PutMapping
    public String updateBook(@RequestBody BookRequestModel model) {
        UpdateBookCommand command = new UpdateBookCommand(model.getBookId(), model.getName(), model.getAuthor(),model.getIsReady());
        commandGateway.sendAndWait(command);
        return "updated book";
    }
    @DeleteMapping("/{bookId}")
    public String deleteBook(@PathVariable String bookId) {
        commandGateway.sendAndWait(new DeleteBookCommand(bookId));
        return "deleted book";
    }

}