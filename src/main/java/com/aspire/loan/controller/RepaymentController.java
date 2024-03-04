package com.aspire.loan.controller;

import com.aspire.common.exceptions.*;
import com.aspire.loan.dto.RepaymentRequestDTO;
import com.aspire.loan.dto.RepaymentResponseDTO;
import com.aspire.loan.model.LoanEntity;
import com.aspire.loan.repository.LoanRepository;
import com.aspire.loan.service.RepaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/mini-aspire/api/v1/repayment")
public class RepaymentController {

    @Autowired
    private final RepaymentService repaymentService;
    @Autowired
    private final LoanRepository loanRepository;

    @PreAuthorize("hasAuthority('USER')")
    @PutMapping
    public ResponseEntity<RepaymentResponseDTO> makeRepayment(@Valid @RequestBody RepaymentRequestDTO repaymentRequest,
                                                              @AuthenticationPrincipal UserDetails userDetails) throws AspireApiException {
        validateRequest(
                repaymentRequest.getLoanId(),
                userDetails.getUsername()
        );
        LoanEntity loan = repaymentService.makeRepayment(repaymentRequest);
        return new ResponseEntity<>(
                new RepaymentResponseDTO(
                        loan,
                        "Repayment instalment paid successfully"
                ),
                HttpStatus.OK
        );
    }

    private void validateRequest(Long loanId,
                                 String username) throws LoanNotFoundException, ResourceNotAuthorizedException {
        LoanEntity loan = loanRepository
                .findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException("Loan not found"));

        if(!username.equals(loan.getUser().getUsername())) {
            throw new ResourceNotAuthorizedException("You don't have access to this loan");
        }
    }

}
