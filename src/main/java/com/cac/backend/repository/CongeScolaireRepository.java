package com.cac.backend.repository;

import com.cac.backend.entity.CongeScolaire;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CongeScolaireRepository extends JpaRepository<CongeScolaire, Long> {
    List<CongeScolaire> findAllByOrderByOrdreAsc();
    List<CongeScolaire> findBySaison(String saison);
}
