package com.rishabh.ecommerce.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import com.rishabh.ecommerce.entities.Role;
import com.rishabh.ecommerce.entities.User;
import com.rishabh.ecommerce.services.JwtService;

public class JwtServiceTest {
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();

        String base64TestKey = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
        ReflectionTestUtils.setField(jwtService, "secretString", base64TestKey);
    }

    @Test
    void generateToken_ShouldReturnValidToken_AndExtractUsername() {
        User mockUser = new User();
        mockUser.setUsername("newuser");
        mockUser.setRole(Role.USER);

        String token = jwtService.generateToken(mockUser);

        assertNotNull(token);

        String extractedUsername = jwtService.extractUsername(token);
        assertEquals("newuser", extractedUsername);
    }

    @Test
    void isTokenValid_ShouldReturnTrue_WhenUsernamesMatch() {
        User mockUser = new User();
        mockUser.setUsername("newuser");
        mockUser.setRole(Role.USER);

        String token = jwtService.generateToken(mockUser);

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername("newuser")
                .password("12345")
                .authorities(new ArrayList<>())
                .build();

        boolean isValid = jwtService.isTokenValid(token, userDetails);

        assertTrue(isValid);

    }
}
