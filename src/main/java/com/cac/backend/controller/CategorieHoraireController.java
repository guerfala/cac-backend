package com.cac.backend.controller;

import com.cac.backend.entity.CategorieHoraire;
import com.cac.backend.repository.CategorieHoraireRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategorieHoraireController {

    private final CategorieHoraireRepository repo;

    @GetMapping
    public List<CategorieHoraire> getAll() {
        return repo.findAllByOrderByOrdreAsc();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategorieHoraire> getById(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public CategorieHoraire create(@RequestBody CategorieHoraire data) {
        return repo.save(data);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategorieHoraire> update(@PathVariable Long id, @RequestBody CategorieHoraire data) {
        return repo.findById(id).map(c -> {
            c.setNom(data.getNom());
            c.setHoraires(data.getHoraires());
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
