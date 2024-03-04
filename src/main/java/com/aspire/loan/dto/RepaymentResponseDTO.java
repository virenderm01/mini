package com.aspire.loan.dto;

import com.aspire.loan.model.LoanEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RepaymentResponseDTO {
    private LoanEntity loan;
    private String message;     // Will hold the message if the loan is fully paid, remaining amount, etc
}

