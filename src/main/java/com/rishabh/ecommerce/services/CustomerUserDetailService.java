package com.rishabh.ecommerce.services;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rishabh.ecommerce.entities.User;
import com.rishabh.ecommerce.repositories.UserRepository;
import com.rishabh.ecommerce.util.AuthorityUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class CustomerUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(),
                List.of(new SimpleGrantedAuthority(AuthorityUtil.toAuthority(user.getRole()))));
    }

}
