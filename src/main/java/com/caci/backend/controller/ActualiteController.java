package com.caci.backend.controller;

import com.caci.backend.entity.Actualite;
import com.caci.backend.repository.ActualiteRepository;
import com.caci.backend.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/actualites")
@RequiredArgsConstructor
public class ActualiteController {

    private final ActualiteRepository repo;
    private final FileStorageService fileService;

    @GetMapping
    public List<Actualite> getAll() {
        return repo.findAllByOrderByDateDesc();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Actualite> getById(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Actualite create(
            @RequestParam String titre,
            @RequestParam(required = false) String extrait,
            @RequestParam(required = false) String contenu,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) MultipartFile image) throws IOException {

        Actualite a = Actualite.builder()
                .titre(titre).extrait(extrait).contenu(contenu)
                .date(date != null ? LocalDate.parse(date) : LocalDate.now())
                .tag(tag)
                .build();

        if (image != null && !image.isEmpty()) {
            a.setImage(fileService.store(image, "actualites"));
        }
        return repo.save(a);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Actualite> update(
            @PathVariable Long id,
            @RequestParam String titre,
            @RequestParam(required = false) String extrait,
            @RequestParam(required = false) String contenu,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) MultipartFile image) throws IOException {

        return repo.findById(id).map(a -> {
            a.setTitre(titre);
            a.setExtrait(extrait);
            a.setContenu(contenu);
            a.setTag(tag);
            if (date != null) a.setDate(LocalDate.parse(date));
            if (image != null && !image.isEmpty()) {
                fileService.delete(a.getImage());
                try { a.setImage(fileService.store(image, "actualites")); }
                catch (IOException e) { throw new RuntimeException(e); }
            }
            return ResponseEntity.ok(repo.save(a));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return repo.findById(id).map(a -> {
            fileService.delete(a.getImage());
            repo.delete(a);
            return ResponseEntity.ok().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
