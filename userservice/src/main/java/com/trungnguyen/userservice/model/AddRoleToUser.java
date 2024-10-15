package com.trungnguyen.userservice.model;

import lombok.Data;

@Data
public class AddRoleToUser {
    private String username;
    private String rolename;
}
