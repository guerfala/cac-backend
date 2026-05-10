package com.cac.backend.repository;

import com.cac.backend.entity.Dirigeant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DirigiantRepository extends JpaRepository<Dirigeant, Long> {
    List<Dirigeant> findAllByOrderByOrdreAsc();
}
