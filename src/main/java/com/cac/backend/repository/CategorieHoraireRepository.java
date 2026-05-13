package com.cac.backend.repository;

import com.cac.backend.entity.CategorieHoraire;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CategorieHoraireRepository extends JpaRepository<CategorieHoraire, Long> {
    List<CategorieHoraire> findAllByOrderByOrdreAsc();
}
