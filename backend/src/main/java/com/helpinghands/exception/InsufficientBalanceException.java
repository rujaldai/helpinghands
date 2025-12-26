package com.helpinghands.exception;

import java.math.BigDecimal;

public class InsufficientBalanceException extends RuntimeException {
    private final BigDecimal availableBalance;
    
    public InsufficientBalanceException(BigDecimal availableBalance) {
        super("Insufficient balance. Available: " + availableBalance);
        this.availableBalance = availableBalance;
    }
    
    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }
}

