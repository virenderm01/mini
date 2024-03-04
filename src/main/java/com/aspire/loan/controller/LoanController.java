package com.aspire.loan.controller;

import com.aspire.common.exceptions.UserNotFoundException;
import com.aspire.loan.dto.LoanRequestDTO;
import com.aspire.loan.dto.LoanResponseDTO;
import com.aspire.loan.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/mini-aspire/api/v1/loan")
public class LoanController {
    @Autowired
    private LoanService loanService;

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping
    public ResponseEntity<LoanResponseDTO> createLoan(@Valid @RequestBody LoanRequestDTO loanRequest,
                                                      @AuthenticationPrincipal UserDetails userDetails) throws UserNotFoundException {
        if(userDetails.getAuthorities().stream().findFirst().get().getAuthority().equals("USER")) {
            log.info("user is creating loan");
        }
        LoanResponseDTO loanResponseDTO = loanService.createLoan(
                loanRequest,
                userDetails.getUsername()
        );
        return new ResponseEntity<>(
                loanResponseDTO,
                HttpStatus.CREATED
        );
    }
}
