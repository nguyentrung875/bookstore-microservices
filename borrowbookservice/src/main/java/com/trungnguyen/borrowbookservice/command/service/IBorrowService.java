package com.trungnguyen.borrowbookservice.command.service;

import com.trungnguyen.borrowbookservice.command.model.Message;

public interface IBorrowService {
    void sendMessage(Message message);
    String findIdBorrowing(String employeeId,String bookId);
}
