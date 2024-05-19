package com.example.patronus.security;

import com.example.patronus.models.entity.User;
import com.example.patronus.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Override
    public CustomUserDetails loadUserByUsername(String username) {
        User user = userService.getUser(username);
        List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .toList();

        return mapUserToCustomUserDetails(user, authorities);
    }

    public CustomUserDetails mapUserToCustomUserDetails(User user, List<SimpleGrantedAuthority> authorities) {
        CustomUserDetails customUserDetails = new CustomUserDetails();
        customUserDetails.setId(user.getId());
        customUserDetails.setUsername(user.getUsername());
        customUserDetails.setPassword(user.getPassword());
        customUserDetails.setName(user.getName());
        customUserDetails.setEmail(user.getEmail());
        customUserDetails.setAuthorities(authorities);
        return customUserDetails;
    }
}
