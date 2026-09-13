package com.jobtracker.job_tracker_backend.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", "test-secret-key-must-be-at-least-256-bits-long-for-hs256");
        ReflectionTestUtils.setField(jwtUtil, "expiration", 86400000L);
    }

    @Test
    void generateToken_shouldProduceTokenContainingCorrectEmail() {
        String token = jwtUtil.generateToken("test@example.com");

        String extractedEmail = jwtUtil.extractEmail(token);

        assertThat(extractedEmail).isEqualTo("test@example.com");
    }

    @Test
    void isTokenValid_shouldReturnTrue_forMatchingEmailAndUnexpiredToken() {
        String token = jwtUtil.generateToken("test@example.com");

        boolean valid = jwtUtil.isTokenValid(token, "test@example.com");

        assertThat(valid).isTrue();
    }

    @Test
    void isTokenValid_shouldReturnFalse_forMismatchedEmail() {
        String token = jwtUtil.generateToken("test@example.com");

        boolean valid = jwtUtil.isTokenValid(token, "different@example.com");

        assertThat(valid).isFalse();
    }

    @Test
    void isTokenValid_shouldReturnFalse_forExpiredToken() {
        ReflectionTestUtils.setField(jwtUtil, "expiration", -1000L);
        String expiredToken = jwtUtil.generateToken("test@example.com");

        boolean valid = jwtUtil.isTokenValid(expiredToken, "test@example.com");

        assertThat(valid).isFalse();
    }
}