package com.cac.backend.repository;

import com.cac.backend.entity.Entraineur;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EntraineurRepository extends JpaRepository<Entraineur, Long> {
    List<Entraineur> findAllByOrderByOrdreAsc();
}
