package com.aspire.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDTO {
    private Long userId;
    private String username;
    private String email;
    private String role;
    private Boolean enabled;
    private String token;
}
