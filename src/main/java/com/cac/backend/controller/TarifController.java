package com.cac.backend.controller;

import com.cac.backend.entity.Tarif;
import com.cac.backend.repository.TarifRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tarifs")
@RequiredArgsConstructor
public class TarifController {

    private final TarifRepository repo;

    @GetMapping
    public List<Tarif> getAll() {
        return repo.findAllByOrderByOrdreAsc();
    }

    @PostMapping
    public Tarif create(@RequestBody Tarif data) {
        return repo.save(data);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tarif> update(@PathVariable Long id, @RequestBody Tarif data) {
        return repo.findById(id).map(t -> {
            t.setCategorie(data.getCategorie());
            t.setCotisation(data.getCotisation());
            t.setLicence(data.getLicence());
            t.setTotal(data.getTotal());
            t.setOrdre(data.getOrdre());
            return ResponseEntity.ok(repo.save(t));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
