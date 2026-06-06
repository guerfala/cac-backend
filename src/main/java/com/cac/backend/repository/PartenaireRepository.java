package com.cac.backend.repository;

import com.cac.backend.entity.Partenaire;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PartenaireRepository extends JpaRepository<Partenaire, Long> {
    List<Partenaire> findAllByOrderByOrdreAsc();
}
