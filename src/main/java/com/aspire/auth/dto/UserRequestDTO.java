package com.aspire.auth.dto;

import lombok.Data;

@Data
public class UserRequestDTO {
    private String username;
    private String email;
    private String password;
    private String role;
    private String enabled;
}
