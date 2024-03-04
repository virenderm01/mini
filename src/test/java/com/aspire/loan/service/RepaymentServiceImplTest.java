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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RepaymentServiceImplTest {
    @InjectMocks
    private RepaymentServiceImpl repaymentService;
    @Mock
    private RepaymentRepository repaymentRepository;
    @Mock
    private LoanRepository loanRepository;


    @Test
    public void testRepaymentSuccess() throws RepaymentAmountInvalidException, RepaymentAlreadyPaidException, InvalidStateException, LoanNotFoundException {
        Long loanId = 1L;
        Double repaymentAmount = 2000.0;

        LoanEntity loan = createLoanEntity(loanId);

        RepaymentEntity repaymentEntity = createRepaymentEntity(loan);

        when(loanRepository.findById(loanId)).thenReturn(Optional.of(loan));
        when(repaymentRepository
                .findFirstByLoanIdAndStatusOrderByDueDateAsc(loanId, RepaymentStatus.PENDING))
                .thenReturn(Optional.of(repaymentEntity));
        when(repaymentRepository.save(any())).thenReturn(repaymentEntity);
        when(loanRepository.save(any())).thenReturn(loan);

        RepaymentRequestDTO repaymentRequest = new RepaymentRequestDTO(loanId, repaymentAmount);
        LoanEntity updatedLoan = repaymentService.makeRepayment(repaymentRequest);

        assertEquals(RepaymentStatus.PAID, repaymentEntity.getStatus());
        assertEquals(2000.0, repaymentEntity.getAmountPaid(), 0.2);
        assertEquals(2000.0, loan.getAmountPaid(), 0.2);
        assertEquals(LoanStatus.APPROVED, updatedLoan.getStatus());

        verify(loanRepository, times(1)).findById(loanId);
        verify(repaymentRepository, times(1)).findFirstByLoanIdAndStatusOrderByDueDateAsc(loanId, RepaymentStatus.PENDING);
        verify(repaymentRepository, times(1)).save(any());
        verify(loanRepository, times(1)).save(any());
    }

    private static RepaymentEntity createRepaymentEntity(LoanEntity loan) {
        RepaymentEntity repaymentEntity = new RepaymentEntity();
        repaymentEntity.setId(1L);
        repaymentEntity.setDueDate(LocalDate.now());
        repaymentEntity.setAmountPaid(0.0);
        repaymentEntity.setAmountDue(2000.0);
        repaymentEntity.setStatus(RepaymentStatus.PENDING);
        repaymentEntity.setLoan(loan);

        return repaymentEntity;
    }

    private static LoanEntity createLoanEntity(Long loanId) {
        LoanEntity loan = new LoanEntity();
        loan.setId(loanId);
        loan.setAmountRequired(6000.0);
        loan.setAmountPaid(0.0);
        loan.setTerm(3);
        loan.setStatus(LoanStatus.APPROVED);
        return loan;
    }

    @Test
    public void testRepaymentWithInvalidRepaymentAmountException() {
        Long loanId = 1L;
        Double repaymentAmount = 1000.0;

        LoanEntity loan = createLoanEntity(loanId);

        when(loanRepository.findById(loanId)).thenReturn(Optional.of(loan));

        RepaymentRequestDTO repaymentRequest = new RepaymentRequestDTO(loanId, repaymentAmount);

        assertThrows(RepaymentAmountInvalidException.class, () -> repaymentService.makeRepayment(repaymentRequest));
        verify(loanRepository, times(1)).findById(loanId);
        verify(repaymentRepository, never()).findFirstByLoanIdAndStatusOrderByDueDateAsc(any(), any());
        verify(repaymentRepository, never()).save(any());
        verify(loanRepository, never()).save(any());
    }

    @Test
    public void testMakeRepaymentLoanNotFoundException() {
        Long loanId = 1L;
        Double repaymentAmount = 2000.0;

        when(loanRepository.findById(loanId)).thenReturn(Optional.empty());

        RepaymentRequestDTO repaymentRequest = new RepaymentRequestDTO(loanId, repaymentAmount);

        assertThrows(LoanNotFoundException.class, () -> repaymentService.makeRepayment(repaymentRequest));
        verify(loanRepository, times(1)).findById(loanId);
        verify(repaymentRepository, never()).findFirstByLoanIdAndStatusOrderByDueDateAsc(any(), any());
        verify(repaymentRepository, never()).save(any());
        verify(loanRepository, never()).save(any());
    }

    @Test
    public void testMakeRepaymentAllRepaymentsPaidSuccess() throws RepaymentAmountInvalidException, RepaymentAlreadyPaidException, InvalidStateException, LoanNotFoundException {
        Long loanId = 1L;
        Double repaymentAmount = 2000.0;

        LoanEntity loan = new LoanEntity();
        loan.setId(loanId);
        loan.setAmountRequired(2000.0);
        loan.setAmountPaid(0.0);
        loan.setTerm(1);
        loan.setStatus(LoanStatus.APPROVED);

        RepaymentEntity repaymentEntity = createRepaymentEntity(loan);

        when(loanRepository.findById(loanId)).thenReturn(Optional.of(loan));
        when(repaymentRepository.findFirstByLoanIdAndStatusOrderByDueDateAsc(loanId, RepaymentStatus.PENDING))
                .thenReturn(Optional.of(repaymentEntity));
        when(loanRepository.save(loan)).thenReturn(loan);

        RepaymentRequestDTO repaymentRequest = new RepaymentRequestDTO(loanId, repaymentAmount);
        LoanEntity updatedLoan = repaymentService.makeRepayment(repaymentRequest);

        assertEquals(LoanStatus.PAID, updatedLoan.getStatus());

        verify(loanRepository, times(1)).findById(loanId);
        verify(repaymentRepository, times(1)).findFirstByLoanIdAndStatusOrderByDueDateAsc(loanId, RepaymentStatus.PENDING);
        verify(repaymentRepository, times(1)).save(any());
        verify(loanRepository, times(2)).save(any());
    }
}
