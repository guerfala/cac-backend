package com.cac.backend.controller;

import com.cac.backend.entity.CongeScolaire;
import com.cac.backend.repository.CongeScolaireRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conges")
@RequiredArgsConstructor
public class CongeScolaireController {

    private final CongeScolaireRepository repo;

    @GetMapping
    public List<CongeScolaire> getAll() {
        return repo.findAllByOrderByOrdreAsc();
    }

    @GetMapping("/saison/{saison}")
    public List<CongeScolaire> getBySaison(@PathVariable String saison) {
        return repo.findBySaison(saison);
    }

    @PostMapping
    public CongeScolaire create(@RequestBody CongeScolaire conge) {
        return repo.save(conge);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CongeScolaire> update(@PathVariable Long id, @RequestBody CongeScolaire data) {
        return repo.findById(id).map(c -> {
            c.setNom(data.getNom());
            c.setDateDebut(data.getDateDebut());
            c.setDateFin(data.getDateFin());
            c.setSaison(data.getSaison());
            c.setIcon(data.getIcon());
            c.setOrdre(data.getOrdre());
            return ResponseEntity.ok(repo.save(c));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
