package com.cac.backend.repository;

import com.cac.backend.entity.SiteSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SiteSettingRepository extends JpaRepository<SiteSetting, Long> {
    Optional<SiteSetting> findByCle(String cle);
}
