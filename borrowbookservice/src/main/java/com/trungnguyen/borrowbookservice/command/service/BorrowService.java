package com.trungnguyen.borrowbookservice.command.service;

import com.trungnguyen.borrowbookservice.command.data.BorrowRepository;
import com.trungnguyen.borrowbookservice.command.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BorrowService implements IBorrowService{
    @Autowired
    private BorrowRepository repository;

    @Override
    public void sendMessage(Message message) {

    }

    @Override
    public String findIdBorrowing(String employeeId, String bookId) {
        return	repository.findByEmployeeIdAndBookIdAndReturnDateIsNull(employeeId,bookId).getId();
    }
}
