package com.example.patronus.logging;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "logs")
public class LogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(columnDefinition = "TEXT")
    private String message;

    private String endpoint;

    private String method;

    @Enumerated(EnumType.STRING)
    private HttpStatus status;

    private String userInfo;

    private String errorType;

    @Column(columnDefinition = "TEXT")
    private String response;

    private String operation;

    private LocalDateTime time;
}
