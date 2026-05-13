package com.cac.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "site_settings")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class SiteSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String cle;

    @Column(columnDefinition = "TEXT")
    private String valeur;
}
