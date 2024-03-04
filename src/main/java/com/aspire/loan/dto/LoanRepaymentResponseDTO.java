package com.aspire.loan.dto;

import com.aspire.loan.constants.LoanStatus;
import com.aspire.loan.constants.RepaymentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
public class LoanRepaymentResponseDTO {
    @JsonFormat(pattern="dd-MM-yyyy")
    private Date repaymentDate;
    private Double repaymentAmount;
    private RepaymentStatus status;

}
