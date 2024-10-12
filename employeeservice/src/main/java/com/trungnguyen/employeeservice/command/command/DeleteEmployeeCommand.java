package com.trungnguyen.employeeservice.command.command;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteEmployeeCommand {
    @TargetAggregateIdentifier
    private String employeeId;

}
