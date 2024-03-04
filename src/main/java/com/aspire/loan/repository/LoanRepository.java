package com.aspire.loan.repository;

import com.aspire.loan.constants.LoanStatus;
import com.aspire.loan.model.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<LoanEntity, Long> {

    List<LoanEntity> findByStatusIn(List<LoanStatus> statuses);

    List<LoanEntity> findByUserId(Long userId);

    List<LoanEntity> findByUserIdAndStatusIn(Long userId,
                                             List<LoanStatus> statuses);
}
