package com.trungnguyen.borrowbookservice.command.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trungnguyen.borrowbookservice.command.command.CreateBorrowCommand;
import com.trungnguyen.borrowbookservice.command.command.UpdateBookReturnCommand;
import com.trungnguyen.borrowbookservice.command.model.BorrowRequestModel;
import com.trungnguyen.borrowbookservice.command.model.Message;
import com.trungnguyen.borrowbookservice.command.service.IBorrowService;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/borrowing")
//@EnableBinding(Source.class)
public class BorrowCommandController {

    @Autowired
    private IBorrowService borrowService;

    @Autowired
    private CommandGateway commandGateway;

    @PostMapping
    public String addBookBorrowing(@RequestBody BorrowRequestModel model) {
        try {
            CreateBorrowCommand command =
                    new CreateBorrowCommand(
                            UUID.randomUUID().toString(),
                            model.getBookId(),
                            model.getEmployeeId(),
                            new Date());
            System.out.println("test command in controller: " + command);
            commandGateway.sendAndWait(command);
            return "Book borrowing added";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "Book borrowing add failed: " + e.getMessage();
        }

    }
    @PutMapping
    public String updateBookReturn(@RequestBody BorrowRequestModel model) {
        UpdateBookReturnCommand command = new UpdateBookReturnCommand(borrowService.findIdBorrowing(model.getEmployeeId(), model.getBookId()), model.getBookId(),model.getEmployeeId(),new Date());
        commandGateway.sendAndWait(command);
        return "Book returned";
    }

//    @PostMapping("/sendMessage")
//    public void SendMessage(@RequestBody Message message) {
//        try {
//
//            ObjectMapper mapper = new ObjectMapper();
//            String json = mapper.writeValueAsString(message);
//            output.send(MessageBuilder.withPayload(json).build());
//        } catch (JsonProcessingException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }

}
