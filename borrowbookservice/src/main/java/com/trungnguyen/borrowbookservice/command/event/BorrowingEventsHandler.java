package com.trungnguyen.borrowbookservice.command.event;

import com.trungnguyen.borrowbookservice.command.data.BorrowRepository;
import com.trungnguyen.borrowbookservice.command.data.BorrowingEntity;
import com.trungnguyen.borrowbookservice.command.service.IBorrowService;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BorrowingEventsHandler {

    @Autowired
    private BorrowRepository borrowRepository;

    @Autowired
    private IBorrowService borrowService;

    @EventHandler
    public void on(BorrowCreatedEvent event) {
        BorrowingEntity model = new BorrowingEntity();

        BeanUtils.copyProperties(event, model);

        var s = borrowRepository.save(model);
        System.out.println("BorrowingEntity: " + s);
    }

    @EventHandler
    public void on(BorrowDeletedEvent event){
        borrowRepository.deleteById(event.getId());
    }

    @EventHandler
    public void on(BorrowingUpdateBookReturnEvent event) {
        BorrowingEntity model = borrowRepository.findByEmployeeIdAndBookIdAndReturnDateIsNull(event.getEmployee(), event.getBookId());
        model.setReturnDate(event.getReturnDate());
        borrowRepository.save(model);
    }
//    @EventHandler
//    public void on(BorrowSendMessageEvent event) {
//        Message message = new Message(event.getEmployeeId(), event.getMessage());
//        borrowService.sendMessage(message);
//    }
}
