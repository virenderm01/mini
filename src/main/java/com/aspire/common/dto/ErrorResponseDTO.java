package com.aspire.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDTO {

    private int statusCode;
    private String message;

    public ErrorResponseDTO(String message)
    {
        super();
        this.message = message;
    }
}