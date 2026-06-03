package com.rishabh.ecommerce.services;

import java.util.List;

import com.rishabh.ecommerce.dto.UserRequest;
import com.rishabh.ecommerce.dto.UserResponse;

public interface UserService {
    UserResponse createUser(UserRequest request);

    List<UserResponse> gellAllUsers();

    UserResponse getUserById(Long id);

    UserResponse updateUser(Long id, UserRequest request);

    void deleteUser(Long id);

}
