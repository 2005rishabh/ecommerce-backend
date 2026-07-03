package com.rishabh.ecommerce.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import com.rishabh.ecommerce.entities.Role;
import com.rishabh.ecommerce.entities.User;
import com.rishabh.ecommerce.repositories.UserRepository;
import com.rishabh.ecommerce.services.CustomerUserDetailService;

@ExtendWith(MockitoExtension.class)

public class CustomerUserDetailServiceTest {
    // will create a fake repo
    @Mock
    UserRepository userRepository;

    @InjectMocks
    CustomerUserDetailService customerUserDetailService;

    @Test
    void loadUserByUsername_ShouldLoadUserDetails_WhenUserExists() {
        String testUsername = "tester";
        String testEmail = "test@gmail.com";
        User mockUser = new User();
        mockUser.setUsername(testUsername);
        mockUser.setEmail(testEmail);
        mockUser.setPassword("fsffasfreefsfer3454ggd");
        mockUser.setRole(Role.USER);
 
        when(userRepository.findByUsername(testUsername)).thenReturn(Optional.of(mockUser));

        UserDetails result = customerUserDetailService.loadUserByUsername(testUsername);

        assertNotNull(result);
        assertEquals(testUsername, result.getUsername());
        assertEquals(mockUser.getPassword(), result.getPassword());

        verify(userRepository, times(1)).findByUsername(testUsername);
    }
}
