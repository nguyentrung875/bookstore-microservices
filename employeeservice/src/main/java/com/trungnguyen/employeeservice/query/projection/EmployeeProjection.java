package com.trungnguyen.employeeservice.query.projection;

import com.trungnguyen.employeeservice.command.data.Employee;
import com.trungnguyen.employeeservice.command.data.EmployeeRepository;
import com.trungnguyen.employeeservice.query.model.EmployeeReponseModel;
import com.trungnguyen.employeeservice.query.queries.GetAllEmployeeQuery;
import com.trungnguyen.employeeservice.query.queries.GetEmployeesQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EmployeeProjection {

    @Autowired
    private EmployeeRepository employeeRepository;

    @QueryHandler
    public EmployeeReponseModel handle(GetEmployeesQuery getEmployeesQuery) {
        EmployeeReponseModel model = new EmployeeReponseModel();
        Employee employee = employeeRepository.findById(getEmployeesQuery.getEmplyeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found!"));
        BeanUtils.copyProperties(employee, model);
        return model;
    }

    @QueryHandler
    public List<EmployeeReponseModel> handle(GetAllEmployeeQuery getAllEmployeeQuery){
        List<EmployeeReponseModel> listModel = new ArrayList<>();
        List<Employee> listEntity = employeeRepository.findAll();
        listEntity.stream().forEach(s -> {
            EmployeeReponseModel model = new EmployeeReponseModel();
            BeanUtils.copyProperties(s, model);
            listModel.add(model);
        });
        return listModel;
    }
}
