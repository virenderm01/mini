package com.aspire.loan.service;

import com.aspire.common.exceptions.UserNotFoundException;
import com.aspire.loan.dto.LoanRequestDTO;
import com.aspire.loan.dto.LoanResponseDTO;

import java.util.List;

public interface LoanService {
    LoanResponseDTO createLoan(LoanRequestDTO loanRequest, String username) throws UserNotFoundException;

    List<LoanResponseDTO> getLoansByUserId(Integer userId);
}
