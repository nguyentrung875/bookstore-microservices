package com.trungnguyen.borrowbookservice.command.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowRepository extends JpaRepository<BorrowingEntity, String> {
    List<BorrowingEntity> findByEmployeeIdAndReturnDateIsNull(String employeeId);
    BorrowingEntity findByEmployeeIdAndBookIdAndReturnDateIsNull(String employeeId,String bookId);
}
