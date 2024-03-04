package com.aspire.auth.service;

import com.aspire.common.exceptions.InvalidStateException;
import com.aspire.common.exceptions.LoanNotFoundException;
import com.aspire.loan.model.LoanEntity;

public interface LoanApprovalService {
    LoanEntity approveLoan(Long loanId, boolean approved) throws InvalidStateException, LoanNotFoundException;
}
