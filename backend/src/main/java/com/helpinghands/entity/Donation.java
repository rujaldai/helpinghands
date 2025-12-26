package com.helpinghands.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "donations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Donation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institution_id")
    private Institution institution;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cause_id")
    private Cause cause;
    
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;
    
    @Builder.Default
    @Column(nullable = false)
    private String currency = "USD";
    
    @Builder.Default
    @Column(nullable = false)
    private Boolean toHostCompany = false; // True if donated to host company
    
    @Builder.Default
    @OneToMany(mappedBy = "donation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Statement> statements = new ArrayList<>();
    
    @Builder.Default
    @OneToMany(mappedBy = "donation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MoneyFlow> moneyFlows = new ArrayList<>();
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    @PrePersist
    public void validateDonationTarget() {
        // Allow donations to host company (institution without cause)
        // Or to specific institution/cause
        if (institution == null && cause == null) {
            throw new jakarta.validation.ValidationException("Donation must have either an institution or a cause");
        }
        if (institution != null && cause != null) {
            throw new jakarta.validation.ValidationException("Donation cannot have both institution and cause");
        }
        // Set toHostCompany flag
        if (institution != null && institution.getIsHostCompany() != null && institution.getIsHostCompany()) {
            this.toHostCompany = true;
        }
    }
}

