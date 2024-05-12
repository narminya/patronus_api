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
@Table(name = "streams")
@ToString(exclude = {"user"})
public class Stream {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    @Column(name = "id")
    private UUID id;
    private String caption;
    private String description;
    private String thumbnailUrl;
    private boolean archived;
    private boolean live=true;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @CreatedDate
    private LocalDateTime createdAt = LocalDateTime.now();

}
