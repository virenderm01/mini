package com.aspire.auth.dto;

import com.aspire.loan.constants.LoanStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanApprovalResponseDTO {
    private Long loadId;
    private LoanStatus newStatus;
}
