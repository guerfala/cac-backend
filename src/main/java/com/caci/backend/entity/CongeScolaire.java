package com.caci.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "conges_scolaires")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CongeScolaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private LocalDate dateDebut;

    private LocalDate dateFin;

    private String saison;

    private String icon;

    @Column(name = "ordre")
    private Integer ordre;
}
