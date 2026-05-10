package com.cac.backend.controller;

import com.cac.backend.entity.Dirigeant;
import com.cac.backend.repository.DirigiantRepository;
import com.cac.backend.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/dirigeants")
@RequiredArgsConstructor
public class DirigiantController {

    private final DirigiantRepository repo;
    private final FileStorageService fileService;

    @GetMapping
    public List<Dirigeant> getAll() {
        return repo.findAllByOrderByOrdreAsc();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dirigeant> getById(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Dirigeant create(
            @RequestParam String nom,
            @RequestParam String role,
            @RequestParam(required = false) Integer ordre,
            @RequestParam(required = false) MultipartFile photo) throws IOException {

        Dirigeant d = Dirigeant.builder()
                .nom(nom).role(role).ordre(ordre)
                .build();

        if (photo != null && !photo.isEmpty()) {
            d.setPhoto(fileService.store(photo, "dirigeants"));
        }
        return repo.save(d);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Dirigeant> update(
            @PathVariable Long id,
            @RequestParam String nom,
            @RequestParam String role,
            @RequestParam(required = false) Integer ordre,
            @RequestParam(required = false) MultipartFile photo) throws IOException {

        return repo.findById(id).map(d -> {
            d.setNom(nom);
            d.setRole(role);
            d.setOrdre(ordre);
            if (photo != null && !photo.isEmpty()) {
                fileService.delete(d.getPhoto());
                try { d.setPhoto(fileService.store(photo, "dirigeants")); }
                catch (IOException e) { throw new RuntimeException(e); }
            }
            return ResponseEntity.ok(repo.save(d));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return repo.findById(id).map(d -> {
            fileService.delete(d.getPhoto());
            repo.delete(d);
            return ResponseEntity.ok().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
