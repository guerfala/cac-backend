package com.cac.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "partenaires")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Partenaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    private String logo;

    private String url;

    @Column(name = "ordre")
    private Integer ordre;
}
