package com.trungnguyen.commonservice.model;

import lombok.Data;

@Data
public class EmployeeResponseCommonModel {
    private String employeeId;
    private String firstName;
    private String lastName;
    private String kin;
    private Boolean isDisciplined;
}
