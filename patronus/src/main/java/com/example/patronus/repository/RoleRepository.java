package com.example.patronus.repository;

import com.example.patronus.enums.ERole;
import com.example.patronus.models.jpa.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ERole name);
}

