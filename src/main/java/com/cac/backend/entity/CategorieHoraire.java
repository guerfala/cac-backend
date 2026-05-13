package com.cac.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categories_horaires")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CategorieHoraire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(columnDefinition = "TEXT")
    private String horaires;

    @Column(name = "ordre")
    private Integer ordre;
}
