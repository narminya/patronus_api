package com.example.patronus.models.entity;


import com.example.patronus.models.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "email_confirm_token")
public class EmailConfirmationToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private  LocalDateTime confirmedAt;

    @ManyToOne
    @JoinColumn(nullable = false,name = "user_id")
    private User user;
}
