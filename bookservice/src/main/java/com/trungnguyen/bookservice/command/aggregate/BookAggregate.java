package com.trungnguyen.bookservice.command.aggregate;

import com.trungnguyen.bookservice.command.command.CreateBookCommand;
import com.trungnguyen.bookservice.command.command.DeleteBookCommand;
import com.trungnguyen.bookservice.command.command.UpdateBookCommand;
import com.trungnguyen.bookservice.command.event.BookCreatedEvent;
import com.trungnguyen.bookservice.command.event.BookDeletedEvent;
import com.trungnguyen.bookservice.command.event.BookUpdatedEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Aggregate
@Data
@NoArgsConstructor
public class BookAggregate { //Nhận command từ controller
    @AggregateIdentifier
    private String bookId;

    private String name;
    private String author;
    private Boolean isReady;

    //ADD BOOK
    //Xử lý command mình đã gửi ở controller

    @CommandHandler
    public BookAggregate(CreateBookCommand createBookCommand) {

        BookCreatedEvent bookCreatedEvent = new BookCreatedEvent();

        //Copy tất cả thuộc tinh qua bookCreatedEvent
        BeanUtils.copyProperties(createBookCommand, bookCreatedEvent);

        //Phát đi event đã tạo book thành công => @EventSourcingHandler
        AggregateLifecycle.apply(bookCreatedEvent);
    }

    //UPDATE BOOK
    @CommandHandler
    public void handle(UpdateBookCommand updateBookCommand) {
        BookUpdatedEvent bookUpdatedEvent = new BookUpdatedEvent();

        BeanUtils.copyProperties(updateBookCommand, bookUpdatedEvent);

        AggregateLifecycle.apply(bookUpdatedEvent);
    }

    //DELETE BOOK
    @CommandHandler
    public void handle(DeleteBookCommand deleteBookCommand) {
        BookDeletedEvent bookDeletedEvent = new BookDeletedEvent();
        BeanUtils.copyProperties(deleteBookCommand, bookDeletedEvent);
        AggregateLifecycle.apply(bookDeletedEvent);
    }



    //Lấy dữ liệu của event để cập nhật lại thông tin cho BookAggrgate, ghi lại lịch sử của event đã thay đổi trường dữ liệu nào

    //ADD BOOK
    @EventSourcingHandler
    public void on(BookCreatedEvent event) {
        this.bookId = event.getBookId();
        this.author = event.getAuthor();
        this.isReady = event.getIsReady();
        this.name = event.getName();
    }

    //UPDATE BOOK
    @EventSourcingHandler
    public void on(BookUpdatedEvent event) {
        this.bookId = event.getBookId();
        this.author = event.getAuthor();
        this.name = event.getName();
        this.isReady = event.getIsReady();
    }

    //DELETE BOOK
    @EventSourcingHandler
    public void on(BookDeletedEvent event) {
        this.bookId = event.getBookId();
    }

}
