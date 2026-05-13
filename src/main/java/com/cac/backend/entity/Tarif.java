package com.cac.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tarifs")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Tarif {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String categorie;

    private String cotisation;

    private String licence;

    private String total;

    @Column(name = "ordre")
    private Integer ordre;
}
