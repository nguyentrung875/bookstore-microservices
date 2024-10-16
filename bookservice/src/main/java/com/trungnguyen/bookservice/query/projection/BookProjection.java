package com.trungnguyen.bookservice.query.projection;

import com.trungnguyen.bookservice.command.data.Book;
import com.trungnguyen.bookservice.command.data.BookRepository;
import com.trungnguyen.bookservice.query.model.BookResponseModel;
import com.trungnguyen.bookservice.query.queries.GetAllBooksQuery;
import com.trungnguyen.bookservice.query.queries.GetBookQuery;
import com.trungnguyen.commonservice.model.BookResponseCommonModel;
import com.trungnguyen.commonservice.query.GetDetailsBookQuery;
import com.trungnguyen.commonservice.query.GetListBookQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component //Này giống tầng service dùng để xử lý logic code
public class BookProjection {

    @Autowired
    private BookRepository bookRepository;

    @QueryHandler
    public BookResponseModel handle(GetBookQuery getBooksQuery) {
        BookResponseModel model = new BookResponseModel();
        Book book = bookRepository.findById(getBooksQuery.getBookId()).get();
        BeanUtils.copyProperties(book, model);

        return model;
    }

    @QueryHandler
    public List<BookResponseModel> handle(GetAllBooksQuery getAllBooksQuery) {
        List<BookResponseModel> listBookResponse = new ArrayList<>();

        List<Book> listBookEntity = bookRepository.findAll();
        listBookEntity.forEach(s -> {
            BookResponseModel model = new BookResponseModel();
            BeanUtils.copyProperties(s, model);
            listBookResponse.add(model);
        });
        return listBookResponse;
    }

    @QueryHandler
    public BookResponseCommonModel handle(GetDetailsBookQuery getDetailsBookQuery) {
        BookResponseCommonModel model = new BookResponseCommonModel();
        Book book = bookRepository.getById(getDetailsBookQuery.getBookId());
        BeanUtils.copyProperties(book, model);

        return model;
    }

    @QueryHandler
    public List<BookResponseCommonModel> handle(GetListBookQuery getListBookQuery){
        List<Book> listEntity = bookRepository.findAll();
        List<BookResponseCommonModel> listbook = new ArrayList<>();
        listEntity.forEach(s -> {
            BookResponseCommonModel model = new BookResponseCommonModel();
            BeanUtils.copyProperties(s, model);
            listbook.add(model);
        });
        return listbook;
    }
}
