package com.trungnguyen.borrowbookservice.query.projection;

import com.trungnguyen.borrowbookservice.command.data.BorrowRepository;
import com.trungnguyen.borrowbookservice.command.data.BorrowingEntity;
import com.trungnguyen.borrowbookservice.query.model.BorrowingResponseModel;
import com.trungnguyen.borrowbookservice.query.queries.GetAllBorrowing;
import com.trungnguyen.borrowbookservice.query.queries.GetListBorrowingByEmployeeQuery;
import com.trungnguyen.commonservice.model.BookResponseCommonModel;
import com.trungnguyen.commonservice.model.BorrowingResponseCommonModel;
import com.trungnguyen.commonservice.model.EmployeeResponseCommonModel;
import com.trungnguyen.commonservice.query.GetDetailsBookQuery;
import com.trungnguyen.commonservice.query.GetDetailsEmployeeQuery;
import com.trungnguyen.commonservice.query.GetListBorrowingByEmployee;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BorrowingProjection {
    @Autowired
    private BorrowRepository borrowRepository;

    @Autowired
    private QueryGateway queryGateway;

    @QueryHandler
    public List<BorrowingResponseModel> handle(GetListBorrowingByEmployeeQuery query){
        List<BorrowingResponseModel> list  = new ArrayList<>();
        List<BorrowingEntity> listEntity = borrowRepository.findByEmployeeIdAndReturnDateIsNull(query.getEmployeeId());
        listEntity.forEach(s ->{
            BorrowingResponseModel model = new BorrowingResponseModel();
            BeanUtils.copyProperties(s, model);
            list.add(model);
        });
        return list;
    }
    @QueryHandler
    public List<BorrowingResponseCommonModel> handle(GetListBorrowingByEmployee query){
        List<BorrowingResponseCommonModel> list  = new ArrayList<>();
        List<BorrowingEntity> listEntity = borrowRepository.findByEmployeeIdAndReturnDateIsNull(query.getEmployeeId());
        listEntity.forEach(s ->{
            BorrowingResponseCommonModel model = new BorrowingResponseCommonModel();
            BeanUtils.copyProperties(s, model);
            list.add(model);
        });
        return list;
    }


    @QueryHandler
    public List<BorrowingResponseModel> handle(GetAllBorrowing query){
        List<BorrowingResponseModel> list  = new ArrayList<>();
        List<BorrowingEntity> listEntity = borrowRepository.findAll();
        listEntity.forEach(s ->{
            BorrowingResponseModel model = new BorrowingResponseModel();
            BeanUtils.copyProperties(s, model);
            model.setNameBook(queryGateway.query(new GetDetailsBookQuery(model.getBookId()), ResponseTypes.instanceOf(BookResponseCommonModel.class)).join().getName());
            EmployeeResponseCommonModel employee = queryGateway.query(new GetDetailsEmployeeQuery(model.getEmployeeId()), ResponseTypes.instanceOf(EmployeeResponseCommonModel.class)).join();
            model.setNameEmployee(employee.getFirstName()+" "+employee.getLastName());
            list.add(model);
        });
        return list;
    }
}
