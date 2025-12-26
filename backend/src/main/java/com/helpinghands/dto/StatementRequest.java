package com.helpinghands.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StatementRequest {
    @NotNull
    private Long donationId;
    
    private String message;
}

