package com.rishabh.ecommerce.dto;

import java.time.LocalDateTime;

import com.rishabh.ecommerce.entities.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class UserRequest {

    private String username;

    private String password;

    private String email;

    private Role role;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}