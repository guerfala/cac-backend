package com.cac.backend.controller;

import com.cac.backend.entity.Partenaire;
import com.cac.backend.repository.PartenaireRepository;
import com.cac.backend.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/partenaires")
@RequiredArgsConstructor
public class PartenaireController {

    private final PartenaireRepository repo;
    private final FileStorageService fileService;

    @GetMapping
    public List<Partenaire> getAll() {
        return repo.findAllByOrderByOrdreAsc();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Partenaire> getById(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Partenaire create(
            @RequestParam String nom,
            @RequestParam(required = false) String url,
            @RequestParam(required = false) Integer ordre,
            @RequestParam(required = false) MultipartFile logo) throws IOException {

        Partenaire p = Partenaire.builder().nom(nom).url(url).ordre(ordre).build();
        if (logo != null && !logo.isEmpty()) {
            p.setLogo(fileService.store(logo, "partenaires"));
        }
        return repo.save(p);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Partenaire> update(
            @PathVariable Long id,
            @RequestParam String nom,
            @RequestParam(required = false) String url,
            @RequestParam(required = false) Integer ordre,
            @RequestParam(required = false) MultipartFile logo) throws IOException {

        return repo.findById(id).map(p -> {
            p.setNom(nom);
            p.setUrl(url);
            p.setOrdre(ordre);
            if (logo != null && !logo.isEmpty()) {
                fileService.delete(p.getLogo());
                try { p.setLogo(fileService.store(logo, "partenaires")); }
                catch (IOException e) { throw new RuntimeException(e); }
            }
            return ResponseEntity.ok(repo.save(p));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return repo.findById(id).map(p -> {
            fileService.delete(p.getLogo());
            repo.delete(p);
            return ResponseEntity.ok().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
