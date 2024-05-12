package com.example.patronus.models.jpa;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "likes")
@ToString(exclude = {"user"})
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "stream_id")
    private Stream stream;

    @CreatedDate
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
