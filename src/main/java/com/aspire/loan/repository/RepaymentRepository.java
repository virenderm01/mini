package com.aspire.loan.repository;

import com.aspire.loan.constants.RepaymentStatus;
import com.aspire.loan.model.LoanEntity;
import com.aspire.loan.model.RepaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepaymentRepository extends JpaRepository<RepaymentEntity, Long> {

    List<RepaymentEntity> findByLoanIdIn(List<Long> loanIds);

    Optional<RepaymentEntity> findFirstByLoanIdAndStatusOrderByDueDateAsc(Long loanId,
                                                                          RepaymentStatus repaymentStatus);

    boolean existsByLoanAndStatus(LoanEntity loan, RepaymentStatus repaymentStatus);

    List<RepaymentEntity> findByLoanId(Long id);
}

