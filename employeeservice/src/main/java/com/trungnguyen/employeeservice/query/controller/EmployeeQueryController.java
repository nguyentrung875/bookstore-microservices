package com.trungnguyen.employeeservice.query.controller;

import com.trungnguyen.employeeservice.query.model.EmployeeReponseModel;
import com.trungnguyen.employeeservice.query.queries.GetAllEmployeeQuery;
import com.trungnguyen.employeeservice.query.queries.GetEmployeesQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeQueryController {

    @Autowired
    private QueryGateway queryGateway;

    @GetMapping("/{employeeId}")
    public EmployeeReponseModel getEmployeeDetail(@PathVariable String employeeId) {
        GetEmployeesQuery query = new GetEmployeesQuery();
        query.setEmplyeeId(employeeId);
        return queryGateway.query(query, EmployeeReponseModel.class).join();
    }

    @GetMapping
    public List<EmployeeReponseModel> getAllEmployee(){
        return queryGateway.query(new GetAllEmployeeQuery(), ResponseTypes.multipleInstancesOf(EmployeeReponseModel.class))
                .join();
    }
}
