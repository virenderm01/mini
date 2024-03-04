package com.aspire.auth.controller;

import com.aspire.auth.service.LoanApprovalService;
import com.aspire.auth.dto.LoanApprovalRequestDTO;
import com.aspire.auth.dto.LoanApprovalResponseDTO;
import com.aspire.common.exceptions.InvalidStateException;
import com.aspire.common.exceptions.LoanNotFoundException;
import com.aspire.loan.model.LoanEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mini-aspire/api/v1/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {
    @Autowired
    private LoanApprovalService loanApprovalService;
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping
    public ResponseEntity<LoanApprovalResponseDTO> approveLoan(@RequestBody @Valid LoanApprovalRequestDTO approvalRequestDTO) throws InvalidStateException, LoanNotFoundException {
        LoanEntity loan = loanApprovalService.approveLoan(
                approvalRequestDTO.getLoanId(),
                approvalRequestDTO.isApproved()
        );
        return new ResponseEntity<>(
                new LoanApprovalResponseDTO(
                        loan.getId(),
                        loan.getStatus()
                ),
                HttpStatus.OK
        );
    }
}
