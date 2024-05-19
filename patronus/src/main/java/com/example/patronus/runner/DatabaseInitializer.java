package com.example.patronus.runner;

import com.example.patronus.enums.UserRole;
import com.example.patronus.models.entity.Role;
import com.example.patronus.models.entity.User;
import com.example.patronus.repository.RoleRepository;
import com.example.patronus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    @Override
    public void run(String... args) {
        if (roleRepository.findAll().isEmpty()) {
            roleRepository.saveAll(ROLES);
        }

        Role optionalRole = roleRepository
                .findByName(UserRole.valueOf("USER")).orElseThrow();

        USERS.forEach(c->c.getRoles().add(optionalRole));

        if (userRepository.findAll().isEmpty()) {
            userRepository.saveAll(USERS);
        }
        log.info("Database initialized");
    }

    private static final List<Role> ROLES = Arrays.asList(
            Role.builder().name(UserRole.valueOf("USER")).build(),
            Role.builder().name(UserRole.valueOf("ADMIN")).build()
    );

    private static final List<User> USERS = Arrays.asList(
            User.builder()
                    .username("narminya")
                    .password("1234")
                    .email("narminya@code.edu.az")
                    .roles(new HashSet<>())
                    .build()
    );
}