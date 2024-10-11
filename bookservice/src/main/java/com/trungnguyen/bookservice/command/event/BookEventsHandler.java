package com.trungnguyen.bookservice.command.event;

import com.trungnguyen.bookservice.command.data.Book;
import com.trungnguyen.bookservice.command.data.BookRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//NƠI XỬ LÝ TẬP TRUNG CÁC EVENT VÀ TƯƠNG TÁC VỚI DATABASE
@Component
public class BookEventsHandler {
    @Autowired
    private BookRepository bookRepository;

    @EventHandler
    public void on(BookCreatedEvent event) {
        Book book = new Book();
        BeanUtils.copyProperties(event,book);
        bookRepository.save(book);
    }

    @EventHandler
    public void on(BookUpdatedEvent event) {
        Book book = bookRepository.findById(event.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found!"));
        book.setAuthor(event.getAuthor());
        book.setName(event.getName());
        book.setIsReady(event.getIsReady());
        bookRepository.save(book);
    }

    @EventHandler
    public void on(BookDeletedEvent event) {
        System.out.println("deleteID: " + event.getBookId());
        bookRepository.deleteById(event.getBookId());
    }
}
