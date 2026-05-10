package com.cac.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "entraineurs")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Entraineur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String specialite;

    private String photo;

    @Column(name = "ordre")
    private Integer ordre;
}
