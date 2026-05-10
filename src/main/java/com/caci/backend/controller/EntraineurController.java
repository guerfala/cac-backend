package com.caci.backend.controller;

import com.caci.backend.entity.Entraineur;
import com.caci.backend.repository.EntraineurRepository;
import com.caci.backend.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/entraineurs")
@RequiredArgsConstructor
public class EntraineurController {

    private final EntraineurRepository repo;
    private final FileStorageService fileService;

    @GetMapping
    public List<Entraineur> getAll() {
        return repo.findAllByOrderByOrdreAsc();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Entraineur> getById(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Entraineur create(
            @RequestParam String nom,
            @RequestParam String specialite,
            @RequestParam(required = false) Integer ordre,
            @RequestParam(required = false) MultipartFile photo) throws IOException {

        Entraineur e = Entraineur.builder().nom(nom).specialite(specialite).ordre(ordre).build();
        if (photo != null && !photo.isEmpty()) {
            e.setPhoto(fileService.store(photo, "entraineurs"));
        }
        return repo.save(e);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Entraineur> update(
            @PathVariable Long id,
            @RequestParam String nom,
            @RequestParam String specialite,
            @RequestParam(required = false) Integer ordre,
            @RequestParam(required = false) MultipartFile photo) throws IOException {

        return repo.findById(id).map(e -> {
            e.setNom(nom);
            e.setSpecialite(specialite);
            e.setOrdre(ordre);
            if (photo != null && !photo.isEmpty()) {
                fileService.delete(e.getPhoto());
                try { e.setPhoto(fileService.store(photo, "entraineurs")); }
                catch (IOException ex) { throw new RuntimeException(ex); }
            }
            return ResponseEntity.ok(repo.save(e));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return repo.findById(id).map(e -> {
            fileService.delete(e.getPhoto());
            repo.delete(e);
            return ResponseEntity.ok().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
