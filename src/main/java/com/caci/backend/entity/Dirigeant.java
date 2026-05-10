package com.caci.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dirigeants")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Dirigeant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String role;

    private String photo;

    @Column(name = "ordre")
    private Integer ordre;
}
