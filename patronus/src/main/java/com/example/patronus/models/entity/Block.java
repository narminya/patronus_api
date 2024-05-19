package com.example.patronus.models.entity;

import com.example.patronus.models.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"blocker","blocked"})
public class Block extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "blocker_user_id")
    private User blocker;

    @ManyToOne
    @JoinColumn(name = "blocked_user_id")
    private User blocked;

}