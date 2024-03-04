package com.aspire.auth.service;

import com.aspire.common.exceptions.InvalidStateException;
import com.aspire.common.exceptions.LoanNotFoundException;
import com.aspire.loan.constants.LoanStatus;
import com.aspire.loan.model.LoanEntity;
import com.aspire.loan.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanApprovalServiceImpl implements LoanApprovalService{
    private final LoanRepository loanRepository;
    @Override
    public LoanEntity approveLoan(Long loanId, boolean approved) throws InvalidStateException, LoanNotFoundException {
        LoanEntity loan = loanRepository
                .findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException("Loan not found"));

        if(loan.getStatus() != LoanStatus.PENDING) {
            throw new InvalidStateException("Loan not in status PENDING");
        }
        if (approved) {
            loan.setStatus(LoanStatus.APPROVED);
        } else {
            loan.setStatus(LoanStatus.REJECTED);
        }
        return loanRepository.save(loan);
    }
}
