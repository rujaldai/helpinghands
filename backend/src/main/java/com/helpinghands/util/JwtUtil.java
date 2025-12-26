package com.helpinghands.util;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class JwtUtil {
    public String extractUsername(String jwt) {
        return null;
    }

    public String extractClaim(String jwt, Function<Claims, String> role) {
        return null;
    }

    public boolean validateToken(String jwt, String username) {
        return false;
    }

    public String generateToken(String email, String name) {
        return null;
    }
}
