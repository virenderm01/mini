package com.aspire.loan.service;

import com.aspire.common.exceptions.InvalidStateException;
import com.aspire.common.exceptions.LoanNotFoundException;
import com.aspire.common.exceptions.RepaymentAlreadyPaidException;
import com.aspire.common.exceptions.RepaymentAmountInvalidException;
import com.aspire.loan.constants.LoanStatus;
import com.aspire.loan.constants.RepaymentStatus;
import com.aspire.loan.dto.RepaymentRequestDTO;
import com.aspire.loan.model.LoanEntity;
import com.aspire.loan.model.RepaymentEntity;
import com.aspire.loan.repository.LoanRepository;
import com.aspire.loan.repository.RepaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class RepaymentServiceImpl implements RepaymentService {

    private final RepaymentRepository repaymentRepository;

    private final LoanRepository loanRepository;

    @Override
    @Transactional
    public LoanEntity makeRepayment(RepaymentRequestDTO repaymentRequest) throws RepaymentAmountInvalidException, InvalidStateException, LoanNotFoundException, RepaymentAlreadyPaidException {

        LoanEntity loan = loanRepository
                .findById(repaymentRequest.getLoanId())
                .orElseThrow(() -> new LoanNotFoundException("Loan not found!"));

        if(loan.getStatus().equals(LoanStatus.PENDING) || loan.getStatus().equals(LoanStatus.REJECTED) ) {
            throw new InvalidStateException("Loan not approved.");
        }
        verifyIfRepaymentIsValid(
                loan,
                repaymentRequest.getAmount()
        );

        RepaymentEntity scheduledRepayment = findNextRepaymentTerm(repaymentRequest.getLoanId());

        scheduledRepayment.setStatus(RepaymentStatus.PAID);
        scheduledRepayment.setAmountPaid(scheduledRepayment.getAmountPaid() + repaymentRequest.getAmount());
        repaymentRepository.save(scheduledRepayment);

        loan.setAmountPaid(loan.getAmountPaid() + repaymentRequest.getAmount());
        loanRepository.save(loan);

        loan = updateLoanStatusIfAllRepaymentsPaid(scheduledRepayment.getLoan());

        return loan;

    }

    private RepaymentEntity findNextRepaymentTerm(Long loanId) throws RepaymentAlreadyPaidException {
        return repaymentRepository
                .findFirstByLoanIdAndStatusOrderByDueDateAsc(
                        loanId,
                        RepaymentStatus.PENDING
                )
                .orElseThrow(() -> new RepaymentAlreadyPaidException("Repayment already paid"));
    }

    private void verifyIfRepaymentIsValid(LoanEntity loan,
                                          Double repayAmount) throws RepaymentAmountInvalidException {
        double remainingRepaymentAmount = loan.getAmountRequired() - loan.getAmountPaid();
        double generalMonthlyInstallments = loan.getAmountRequired()/loan.getTerm();
        double minInstallment = Double.min(
                generalMonthlyInstallments,
                remainingRepaymentAmount
        );
        if(minInstallment > repayAmount) {
            throw new RepaymentAmountInvalidException("Repayment amount cannot be less than this week's installment");
        }
    }

    private LoanEntity updateLoanStatusIfAllRepaymentsPaid(LoanEntity loan) {
        boolean repaymentRemaining = (loan.getAmountRequired() > loan.getAmountPaid());
        if (!repaymentRemaining) {
            loan.setStatus(LoanStatus.PAID);
            loan = loanRepository.save(loan);
        }
        return loan;
    }
}

