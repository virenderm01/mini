package com.aspire.loan.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class LoanRequestDTO {
    @Positive(message = "Amount should be positive")
    private Double loanAmount;
    @Min(value = 1, message = "Minimum term must be 1")
    private Integer loanTerm;
}
