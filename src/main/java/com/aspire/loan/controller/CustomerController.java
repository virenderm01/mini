package com.aspire.loan.controller;

import com.aspire.auth.model.UserEntity;
import com.aspire.auth.repository.UserRepository;
import com.aspire.loan.dto.LoanResponseDTO;
import com.aspire.loan.service.LoanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/mini-aspire/api/v1/loan")
public class CustomerController {
    @Autowired
    private final LoanService loanService;

    @Autowired
    private final UserRepository userRepository;

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/{userId}")
    public ResponseEntity<List<LoanResponseDTO>> getLoansByUserId(@PathVariable("userId") Integer userId,
                                                                  @AuthenticationPrincipal UserDetails userDetails) {
        Optional<UserEntity> byUsername = userRepository.findByUsername(userDetails.getUsername());
        if(byUsername.isPresent()){
            UserEntity userEntity = byUsername.get();
            if(userEntity.getId().compareTo(userId.longValue())!= 0){
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }

        List<LoanResponseDTO> loanResponseDTOS = loanService.getLoansByUserId(userId);
        return new ResponseEntity<>(loanResponseDTOS, HttpStatus.OK);
    }
}
