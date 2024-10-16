package com.trungnguyen.borrowbookservice.command.saga;

import com.trungnguyen.borrowbookservice.command.command.DeleteBorrowCommand;
import com.trungnguyen.borrowbookservice.command.event.BorrowCreatedEvent;
import com.trungnguyen.borrowbookservice.command.event.BorrowDeletedEvent;
import com.trungnguyen.borrowbookservice.command.event.BorrowingUpdateBookReturnEvent;
import com.trungnguyen.commonservice.command.RollBackStatusBookCommand;
import com.trungnguyen.commonservice.command.UpdateStatusBookCommand;
import com.trungnguyen.commonservice.events.BookRollBackStatusEvent;
import com.trungnguyen.commonservice.events.BookUpdateStatusEvent;
import com.trungnguyen.commonservice.model.BookResponseCommonModel;
import com.trungnguyen.commonservice.model.EmployeeResponseCommonModel;
import com.trungnguyen.commonservice.query.GetDetailsBookQuery;
import com.trungnguyen.commonservice.query.GetDetailsEmployeeQuery;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.annotation.MessageHandler;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AliasFor;

@Saga
public class BorrowingSaga {

    @Autowired
    private transient CommandGateway commandGateway;

    @Autowired
    private transient QueryGateway queryGateway;

    @StartSaga
    @SagaEventHandler(associationProperty = "id") //target Identify của event đó
    private void handle(BorrowCreatedEvent event) { //Nhận event BorrowCreated
        System.out.println("BorrowCreatedEvent in Saga for BookId : "+event.getBookId()+ " : EmployeeId :  "+event.getEmployeeId());

        try {
            //Bắt đầu SAGA
            SagaLifecycle.associateWith("bookId", event.getBookId());

            //Query command gọi qua bookservice
            GetDetailsBookQuery getDetailsBookQuery = new GetDetailsBookQuery(event.getBookId());

            BookResponseCommonModel bookResponseModel =
                    queryGateway.query(getDetailsBookQuery,
                            ResponseTypes.instanceOf(BookResponseCommonModel.class))
                            .join();

            //Kiểm tra xem sách đã được mượn hay chưa
            if(bookResponseModel.getIsReady()) {
                //Cập nhật trạng thái của sách sang đã được mượn
                UpdateStatusBookCommand command = new UpdateStatusBookCommand(event.getBookId(), false,event.getEmployeeId(),event.getId());
                commandGateway.sendAndWait(command);
            }
            else {

                throw new Exception("SÁCH ĐÃ CÓ NGƯỜI MƯỢN");
            }
        } catch (Exception e) {
            System.out.println("BorrowCreatedEvent SAGA FAILED: " + e.getMessage());

            //rollback borrow record đã lưu
            rollBackBorrowRecord(event.getId());
        }
    }


    @StartSaga
    @SagaEventHandler(associationProperty = "id")
    private void handle(BorrowingUpdateBookReturnEvent event) { //Nhận event UpdateBook để xử lý
        System.out.println("BorrowingUpdateBookReturnEvent in Saga for borrowing Id : "+event.getId());
        try {
            UpdateStatusBookCommand command = new UpdateStatusBookCommand(event.getBookId(), true,event.getEmployee(),event.getId());
            commandGateway.sendAndWait(command);
//            commandGateway.sendAndWait(new SendMessageCommand(event.getId(), event.getEmployee(), "Da tra sach thanh cong !"));
            SagaLifecycle.end();
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
    }


    private void rollBackBorrowRecord(String id) {
        //Xóa record borrow khi transaction ko thành công
        commandGateway.sendAndWait(new DeleteBorrowCommand(id));
    }


    @SagaEventHandler(associationProperty = "bookId")
    private void handle(BookUpdateStatusEvent event) {
        System.out.println("BookUpdateStatusEvent in Saga for BookId : "+event.getBookId());
        try {
            GetDetailsEmployeeQuery getDetailsEmployeeQuery = new GetDetailsEmployeeQuery(event.getEmployeeId());

            EmployeeResponseCommonModel employeeResponseCommonModel =
                    queryGateway.query(getDetailsEmployeeQuery,
                            ResponseTypes.instanceOf(EmployeeResponseCommonModel.class))
                            .join();
            if(employeeResponseCommonModel.getIsDisciplined()) {
                throw new Exception("NHÂN VIÊN BỊ KỶ LUẬT!");
            }else {
                System.out.println("ĐÃ MƯỢN SÁCH THÀNH CÔNG!");
//                commandGateway.sendAndWait(new SendMessageCommand(event.getBorrowId(), event.getEmployeeId(), "Da muon sach thanh cong !"));
                SagaLifecycle.end();
            }
        } catch (Exception e) {

            System.out.println("ERROR BookUpdateStatusEvent: " + e);
            rollBackBookStatus(event.getBookId(),event.getEmployeeId(),event.getBorrowId());
        }
    }

    private void rollBackBookStatus(String bookId,String employeeId,String borrowId) {
        SagaLifecycle.associateWith("bookId", bookId);
        RollBackStatusBookCommand command = new RollBackStatusBookCommand(bookId, true,employeeId,borrowId);
        commandGateway.sendAndWait(command);
    }

    @SagaEventHandler(associationProperty = "bookId")
    public void handleRollBackBookStatus(BookRollBackStatusEvent event) {
        System.out.println("BookRollBackStatusEvent in Saga for book Id : {} " + event.getBookId());
        rollBackBorrowRecord(event.getBorrowId());
    }

    @SagaEventHandler(associationProperty = "id")
    @EndSaga
    public void handle(BorrowDeletedEvent event) {
        System.out.println("BorrowDeletedEvent in Saga for Borrowing Id : {} " +
                event.getId());
        SagaLifecycle.end();
    }
}
