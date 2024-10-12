package com.trungnguyen.employeeservice.command.event;

import com.trungnguyen.employeeservice.command.data.Employee;
import com.trungnguyen.employeeservice.command.data.EmployeeRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmployeeEventsHandler {

    @Autowired
    private EmployeeRepository employeeRepository;

    @EventHandler
    public void on(EmployeeCreatedEvent event) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(event, employee);
        var s = employeeRepository.save(employee);
        System.out.println(s);
    }

    @EventHandler
    public void on (EmployeeUpdatedEvent event) {
        Employee employee = employeeRepository.findById(event.getEmployeeId()).get();
        employee.setFirstName(event.getFirstName());
        employee.setLastName(event.getLastName());
        employee.setKin(event.getKin());
        employee.setIsDisciplined(event.getIsDisciplined());
        employeeRepository.save(employee);
    }

    @EventHandler
    public void on(EmployeeDeletedEvent event) {
        employeeRepository.deleteById(event.getEmployeeId());
    }


}
