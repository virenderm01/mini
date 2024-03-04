package com.aspire.auth.service;



import com.aspire.common.exceptions.InvalidStateException;
import com.aspire.common.exceptions.LoanNotFoundException;
import com.aspire.loan.constants.LoanStatus;
import com.aspire.loan.model.LoanEntity;
import com.aspire.loan.repository.LoanRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class LoanApprovalServiceImplTest {

    @InjectMocks
    private LoanApprovalServiceImpl loanApprovalService;
    @Mock
    private LoanRepository loanRepository;


    @Test
    public void testApproveLoanSuccess() throws InvalidStateException, LoanNotFoundException {
        LoanEntity pendingLoan = new LoanEntity();
        pendingLoan.setStatus(LoanStatus.PENDING);

        when(loanRepository.findById(anyLong())).thenReturn(Optional.of(pendingLoan));
        when(loanRepository.save(any())).thenReturn(pendingLoan);

        LoanEntity approvedLoan = loanApprovalService.approveLoan(1L, true);

        assertEquals(LoanStatus.APPROVED, approvedLoan.getStatus());
        verify(loanRepository, times(1)).findById(anyLong());
        verify(loanRepository, times(1)).save(any());
    }

    @Test
    public void testRejectLoanSuccess() throws InvalidStateException, LoanNotFoundException {
        LoanEntity pendingLoan = new LoanEntity();
        pendingLoan.setStatus(LoanStatus.PENDING);

        when(loanRepository.findById(anyLong())).thenReturn(Optional.of(pendingLoan));
        when(loanRepository.save(any())).thenReturn(pendingLoan);

        LoanEntity rejectedLoan = loanApprovalService.approveLoan(1L, false);

        assertEquals(LoanStatus.REJECTED, rejectedLoan.getStatus());
        verify(loanRepository, times(1)).findById(anyLong());
        verify(loanRepository, times(1)).save(any());
    }

    @Test
    public void testCreateLoanInvalidStateException() {
        LoanEntity approvedLoan = new LoanEntity();
        approvedLoan.setStatus(LoanStatus.APPROVED);

        when(loanRepository.findById(anyLong())).thenReturn(Optional.of(approvedLoan));

        assertThrows(InvalidStateException.class, () -> loanApprovalService.approveLoan(1L, true));
        verify(loanRepository, times(1)).findById(anyLong());
        verify(loanRepository, never()).save(any());
    }

    @Test
    public void testCreateLoanNotFoundException() {
        when(loanRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(LoanNotFoundException.class, () -> loanApprovalService.approveLoan(1L, true));
        verify(loanRepository, times(1)).findById(anyLong());
        verify(loanRepository, never()).save(any());
    }
}
