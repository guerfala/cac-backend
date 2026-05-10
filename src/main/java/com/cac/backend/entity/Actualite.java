package com.cac.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "actualites")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Actualite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    @Column(columnDefinition = "TEXT")
    private String extrait;

    @Column(columnDefinition = "TEXT")
    private String contenu;

    private LocalDate date;

    private String tag;

    private String image;
}
