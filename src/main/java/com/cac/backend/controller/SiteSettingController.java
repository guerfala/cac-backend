package com.cac.backend.controller;

import com.cac.backend.entity.SiteSetting;
import com.cac.backend.repository.SiteSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
public class SiteSettingController {

    private final SiteSettingRepository repo;

    @GetMapping
    public List<SiteSetting> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{cle}")
    public ResponseEntity<SiteSetting> getByCle(@PathVariable String cle) {
        return repo.findByCle(cle)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{cle}")
    public SiteSetting update(@PathVariable String cle, @RequestBody Map<String, String> body) {
        SiteSetting setting = repo.findByCle(cle).orElse(
                SiteSetting.builder().cle(cle).build()
        );
        setting.setValeur(body.get("valeur"));
        return repo.save(setting);
    }
}
