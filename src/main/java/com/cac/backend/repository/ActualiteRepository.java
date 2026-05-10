package com.cac.backend.repository;

import com.cac.backend.entity.Actualite;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ActualiteRepository extends JpaRepository<Actualite, Long> {
    List<Actualite> findAllByOrderByDateDesc();
}
