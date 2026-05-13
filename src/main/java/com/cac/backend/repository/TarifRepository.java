package com.cac.backend.repository;

import com.cac.backend.entity.Tarif;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TarifRepository extends JpaRepository<Tarif, Long> {
    List<Tarif> findAllByOrderByOrdreAsc();
}
