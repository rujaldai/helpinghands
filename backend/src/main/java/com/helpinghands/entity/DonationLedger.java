package com.helpinghands.entity;

import com.helpinghands.enums.Currency;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "money_flows")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class DonationLedger {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donation_id", nullable = false)
    private Donation donation; // Original donation

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_institution_id")
    private Institution fromInstitution;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_institution_id")
    private Institution toInstitution;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cause_id")
    private Cause cause;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expense_id")
    private Expense expense;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transfer_id")
    private Transfer transfer;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount; // Amount from this donation used

    @Column
    private String description;
    
    @Builder.Default
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Currency currency = Currency.USD;
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}

