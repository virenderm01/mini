package com.aspire.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoanApprovalRequestDTO {
    @NotNull
    private Long loanId;
    private boolean approved;
}
