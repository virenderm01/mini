package com.aspire.loan.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RepaymentRequestDTO {
    @NotNull
    private Long loanId;
    @Min(value = 0)
    private Double amount;
}

