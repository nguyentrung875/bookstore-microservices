package com.trungnguyen.employeeservice.query.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeReponseModel {
    private String employeeId;
    private String firstName;
    private String lastName;
    private String kin;
    private Boolean isDisciplined;
}
