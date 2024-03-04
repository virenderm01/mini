package com.aspire.loan.service;

import com.aspire.auth.model.UserEntity;
import com.aspire.auth.repository.UserRepository;
import com.aspire.common.exceptions.UserNotFoundException;
import com.aspire.loan.constants.LoanStatus;
import com.aspire.loan.constants.RepaymentStatus;
import com.aspire.loan.dto.LoanRequestDTO;
import com.aspire.loan.dto.LoanResponseDTO;
import com.aspire.loan.model.LoanEntity;
import com.aspire.loan.model.RepaymentEntity;
import com.aspire.loan.repository.LoanRepository;
import com.aspire.loan.repository.RepaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoanServiceImplTest {

    @InjectMocks
    private LoanServiceImpl loanServiceImpl;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private RepaymentRepository repaymentRepository;


    @Test
    public void testCreateLoanSuccess() throws UserNotFoundException {
        // Create a mock UserEntity and LoanApplicationRequestDTO
        UserEntity user = new UserEntity();
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        LoanRequestDTO loanRequest = new LoanRequestDTO();
        loanRequest.setLoanAmount(10000.0);
        loanRequest.setLoanTerm(3);
        LoanEntity savedLoan = new LoanEntity(
                1L,
                10000.0,
                0.0,
                3,
                LocalDate.now(),
                LoanStatus.PENDING,
                new UserEntity()
        );
        RepaymentEntity repayment = new RepaymentEntity();
        repayment.setDueDate(LocalDate.now());
        repayment.setId(1L);
        repayment.setLoan(savedLoan);
        repayment.setStatus(RepaymentStatus.PENDING);
        repayment.setDueDate(LocalDate.now().plusWeeks(1));
        when(loanRepository.save(ArgumentMatchers.any())).thenReturn(savedLoan);
        when(repaymentRepository.save(ArgumentMatchers.any())).thenReturn(repayment);

        LoanResponseDTO createdLoan = loanServiceImpl.createLoan(loanRequest, "username");

        assertNotNull(createdLoan);
        assertEquals(LoanStatus.PENDING, createdLoan.getLoanStatus());
        verify(loanRepository, Mockito.times(1)).save(ArgumentMatchers.any());
        verify(repaymentRepository, Mockito.times(3)).save(ArgumentMatchers.any());
    }


}
