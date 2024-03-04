package com.aspire.loan.service;

import com.aspire.common.exceptions.InvalidStateException;
import com.aspire.common.exceptions.LoanNotFoundException;
import com.aspire.common.exceptions.RepaymentAlreadyPaidException;
import com.aspire.common.exceptions.RepaymentAmountInvalidException;
import com.aspire.loan.dto.RepaymentRequestDTO;
import com.aspire.loan.model.LoanEntity;

public interface RepaymentService {
    LoanEntity makeRepayment(RepaymentRequestDTO repaymentRequest) throws RepaymentAmountInvalidException, InvalidStateException, LoanNotFoundException, RepaymentAlreadyPaidException;

}
