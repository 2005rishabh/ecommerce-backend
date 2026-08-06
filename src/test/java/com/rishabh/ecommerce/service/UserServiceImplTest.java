package com.rishabh.ecommerce.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.rishabh.ecommerce.dto.UserRequest;
import com.rishabh.ecommerce.dto.UserResponse;
import com.rishabh.ecommerce.entities.Role;
import com.rishabh.ecommerce.entities.User;
import com.rishabh.ecommerce.error.ResourceNotFoundException;
import com.rishabh.ecommerce.error.UserAlreadyExistsException;
import com.rishabh.ecommerce.repositories.UserRepository;
import com.rishabh.ecommerce.services.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserServiceImpl userServiceImpl;

    @Test
    void createUser_ShouldEncodePasswordAndSave_WhenDataIsValid() {
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("newuser");
        userRequest.setEmail("test@gmail.com");
        userRequest.setPassword("testuser");
        userRequest.setRole(Role.USER);

        User savedUser = User.builder()
                .id(1L)
                .username("newuser")
                .email("test@gmail.com")
                .password("hashedPassword123")
                .role(Role.USER)
                .build();

        when(userRepository.existsByUsername(userRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(userRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode("testuser")).thenReturn("hashedPassword123");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponse result = userServiceImpl.createUser(userRequest);

        assertNotNull(result);
        assertEquals("newuser", result.getUsername());
        assertEquals("test@gmail.com", result.getEmail());

        verify(passwordEncoder, times(1)).encode("testuser");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_ShouldThrowException_WhenUsernameExists() {
        UserRequest request = new UserRequest();
        request.setUsername("alreadyAUser");

        when(userRepository.existsByUsername(request.getUsername())).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> {
            userServiceImpl.createUser(request);
        });

        verify(userRepository, never()).existsByEmail(anyString());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUser_ShouldThrowException_WhenEmailExists() {

        UserRequest request = new UserRequest();
        request.setUsername("validUser");
        request.setEmail("taken@gmail.com");

        when(userRepository.existsByUsername(request.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> {
            userServiceImpl.createUser(request);
        });

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getUserById_ShouldReturnUser_WhenExists() {
        Long userId = 1L;
        User mockUser = User.builder().id(userId).username("testuser").build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        UserResponse result = userServiceImpl.getUserById(userId);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void getUserById_ShouldThrowException_WhenUserDoesNotExist() {
        Long invalidId = 99L;
        when(userRepository.findById(invalidId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userServiceImpl.getUserById(invalidId);
        });
    }

    @Test
    void updateUser_ShouldUpdateAndEncodeNewPassword_WhenUserExists() {
        Long userId = 1L;
        
        UserRequest updateRequest = new UserRequest();
        updateRequest.setEmail("updated@gmail.com");
        updateRequest.setPassword("newPlainTextPassword");
        updateRequest.setRole(Role.ADMIN);

        User existingUser = User.builder()
                .id(userId)
                .email("old@gmail.com")
                .password("oldHashedPassword")
                .role(Role.USER)
                .build();

        User savedUser = User.builder()
                .id(userId)
                .email("updated@gmail.com")
                .password("newHashedPassword123")
                .role(Role.ADMIN)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode("newPlainTextPassword")).thenReturn("newHashedPassword123");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponse result = userServiceImpl.updateUser(userId, updateRequest);

        assertEquals("updated@gmail.com", result.getEmail());
        assertEquals(Role.ADMIN, result.getRole());
        
        verify(passwordEncoder, times(1)).encode("newPlainTextPassword");
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void deleteUser_ShouldDelete_WhenUserExists() {
        Long userId = 1L;
        User existingUser = User.builder().id(userId).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        userServiceImpl.deleteUser(userId);

        verify(userRepository, times(1)).delete(existingUser);
    }
}

