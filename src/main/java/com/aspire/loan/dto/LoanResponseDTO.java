package com.aspire.loan.dto;

import com.aspire.loan.constants.LoanStatus;
import lombok.Data;

import java.util.List;
@Data
public class LoanResponseDTO {
    private Long loadId;
    private LoanStatus loanStatus;
    private List<LoanRepaymentResponseDTO> loanRepaymentResponseDTOs;
}
