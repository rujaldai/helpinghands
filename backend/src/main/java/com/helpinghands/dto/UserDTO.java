package com.helpinghands.dto;

import com.helpinghands.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String email;
    private String name;
    private User.Role role;
    private String guestId;
    private Boolean active;
    private LocalDateTime createdAt;
}

