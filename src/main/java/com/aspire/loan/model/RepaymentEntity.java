package com.aspire.loan.model;

import com.aspire.loan.constants.RepaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RepaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private LocalDate dueDate;

    private Double amountPaid;
    private Double amountDue;
    @NonNull
    @Enumerated(EnumType.STRING)
    private RepaymentStatus status;
    @ManyToOne
    @JoinColumn(name = "loan_id", nullable = false)
    private LoanEntity loan;
}
