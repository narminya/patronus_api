package com.example.patronus.runner;

import com.example.patronus.enums.ERole;
import com.example.patronus.models.jpa.Role;
import com.example.patronus.models.jpa.User;
import com.example.patronus.repository.RoleRepository;
import com.example.patronus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
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
                .findByName(ERole.valueOf("USER")).orElseThrow();

        USERS.forEach(c->c.getRoles().add(optionalRole));

        if (userRepository.findAll().isEmpty()) {
            userRepository.saveAll(USERS);
        }
        log.info("Database initialized");
    }

    private static final List<Role> ROLES = Arrays.asList(
            Role.builder().name(ERole.valueOf("USER")).build(),
            Role.builder().name(ERole.valueOf("ADMIN")).build()
    );

    private static final List<User> USERS = Arrays.asList(
            User.builder()
                    .username("narminya")
                    .password("1234")
                    .email("user@mycompany.com")
                    .roles(new HashSet<>())
                    .build()
    );
}