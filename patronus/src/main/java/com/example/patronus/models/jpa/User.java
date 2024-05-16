package com.example.patronus.models.jpa;

import com.example.patronus.enums.UserStatus;
import com.example.patronus.models.base.BaseEntity;
import com.example.patronus.security.oauth.OAuth2Provider;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})

public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;
    private String username;
    private String email;
    private String password;
    private String imageUrl;
    private String bio;
    private String name;
    private String providerId;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private OAuth2Provider provider =  OAuth2Provider.LOCAL;

    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(  name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus = UserStatus.ACTIVE;

    @Builder.Default
    @Column(name = "email_confirmed")
    private boolean emailConfirmed=false;
}
