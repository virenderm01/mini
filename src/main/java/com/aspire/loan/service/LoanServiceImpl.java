package com.aspire.loan.service;

import com.aspire.auth.model.UserEntity;
import com.aspire.auth.repository.UserRepository;
import com.aspire.common.exceptions.UserNotFoundException;
import com.aspire.loan.constants.LoanStatus;
import com.aspire.loan.constants.RepaymentStatus;
import com.aspire.loan.dto.LoanRepaymentResponseDTO;
import com.aspire.loan.dto.LoanRequestDTO;
import com.aspire.loan.dto.LoanResponseDTO;
import com.aspire.loan.model.LoanEntity;
import com.aspire.loan.model.RepaymentEntity;
import com.aspire.loan.repository.LoanRepository;
import com.aspire.loan.repository.RepaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

    private final UserRepository userRepository;
    private final LoanRepository loanRepository;
    private final RepaymentRepository repaymentRepository;

    @Override
    @Transactional
    public LoanResponseDTO createLoan(LoanRequestDTO loanRequest, String username) throws UserNotFoundException {
        log.info(">>> LoanServiceImpl.createLoan {}", loanRequest);
        log.info("--- Creating loan for user: {}", username);
        Optional<UserEntity> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found", HttpStatus.NOT_FOUND);
        }
        LoanEntity loan = LoanEntity.builder()
                .id(null)
                .loanApplicationDate(LocalDate.now())
                .term(loanRequest.getLoanTerm())
                .amountRequired(loanRequest.getLoanAmount())
                .amountPaid(0.0)
                .status(LoanStatus.PENDING)
                .user(user.get())
                .build();

        LoanEntity savedLoan = loanRepository.save(loan);

        // Calculate and save repayments
        List<RepaymentEntity> repayments = calculateAndSaveRepayments(savedLoan);
        log.info("<<< LoanServiceImpl.createLoan {}", loanRequest);
        return createLoanResponseDTO(savedLoan, repayments);
    }

    @Override
    public List<LoanResponseDTO> getLoansByUserId(Integer userId) {
        log.info(">>> LoanServiceImpl.getLoansByUserId {}", userId);
        List<LoanEntity> loans = loanRepository.findByUserId(userId.longValue());
        List<LoanResponseDTO> loanResponseDTOS = new ArrayList<>();
        for (LoanEntity loan : loans) {
            List<RepaymentEntity> repayments = repaymentRepository.findByLoanId(loan.getId());
            LoanResponseDTO loanResponseDTO = createLoanResponseDTO(loan, repayments);
            loanResponseDTOS.add(loanResponseDTO);
        }
        return loanResponseDTOS;
    }

    private LoanResponseDTO createLoanResponseDTO(LoanEntity savedLoan, List<RepaymentEntity> repayments) {
        log.info(">>> LoanServiceImpl.createLoanResponseDTO {}", savedLoan);
        LoanResponseDTO loanResponseDTO = new LoanResponseDTO();
        List<LoanRepaymentResponseDTO> loanRepaymentResponseDTOS = new ArrayList<>();
        for (RepaymentEntity repayment : repayments) {
            log.info("Repayment: {}", repayment);
            LoanRepaymentResponseDTO loanRepaymentResponseDTO = new LoanRepaymentResponseDTO();
            loanRepaymentResponseDTO.setRepaymentDate(Date.from(repayment.getDueDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            loanRepaymentResponseDTO.setRepaymentAmount(repayment.getAmountDue());
            loanRepaymentResponseDTO.setStatus(repayment.getStatus());
            loanRepaymentResponseDTOS.add(loanRepaymentResponseDTO);
        }
        loanResponseDTO.setLoadId(savedLoan.getId());
        loanResponseDTO.setLoanStatus(savedLoan.getStatus());
        loanResponseDTO.setLoanRepaymentResponseDTOs(loanRepaymentResponseDTOS);
        log.info("<<< LoanServiceImpl.createLoanResponseDTO {}", savedLoan);
        return loanResponseDTO;
    }

    @Transactional
    private List<RepaymentEntity> calculateAndSaveRepayments(LoanEntity loan) {
        log.info(">>> LoanServiceImpl.calculateAndSaveRepayments {}", loan);
        int loanTerm = loan.getTerm();
        LocalDate currentDate = LocalDate.now();
        List<RepaymentEntity> repayments = new ArrayList<>();
        Double amountDue = loan.getAmountRequired() / loanTerm;
        for (int i = 1; i <= loanTerm; i++) {
            RepaymentEntity repayment = RepaymentEntity.builder()
                    .dueDate(currentDate.plusWeeks(i))
                    .amountDue(amountDue)
                    .amountPaid(0.0)
                    .status(RepaymentStatus.PENDING)
                    .loan(loan)
                    .build();
            repayment = repaymentRepository.save(repayment);
            repayments.add(repayment);
        }
        log.info("<<< LoanServiceImpl.calculateAndSaveRepayments {}", loan);
        return repayments;
    }
}


