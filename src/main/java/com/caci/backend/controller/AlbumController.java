package com.caci.backend.controller;

import com.caci.backend.entity.Album;
import com.caci.backend.entity.Photo;
import com.caci.backend.repository.AlbumRepository;
import com.caci.backend.repository.PhotoRepository;
import com.caci.backend.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/albums")
@RequiredArgsConstructor
public class AlbumController {

    private final AlbumRepository albumRepo;
    private final PhotoRepository photoRepo;
    private final FileStorageService fileService;

    @GetMapping
    public List<Album> getAll() {
        return albumRepo.findAllByOrderByDateDesc();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getById(@PathVariable Long id) {
        return albumRepo.findById(id).map(album -> {
            Map<String, Object> result = new HashMap<>();
            result.put("id", album.getId());
            result.put("titre", album.getTitre());
            result.put("date", album.getDate());
            result.put("couverture", album.getCouverture());
            result.put("photos", album.getPhotos());
            return ResponseEntity.ok(result);
        }).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Album create(
            @RequestParam String titre,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) MultipartFile couverture) throws IOException {

        Album album = Album.builder()
                .titre(titre)
                .date(date != null ? LocalDate.parse(date) : LocalDate.now())
                .build();

        if (couverture != null && !couverture.isEmpty()) {
            album.setCouverture(fileService.store(couverture, "albums"));
        }
        return albumRepo.save(album);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Album> update(
            @PathVariable Long id,
            @RequestParam String titre,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) MultipartFile couverture) throws IOException {

        return albumRepo.findById(id).map(album -> {
            album.setTitre(titre);
            if (date != null) album.setDate(LocalDate.parse(date));
            if (couverture != null && !couverture.isEmpty()) {
                fileService.delete(album.getCouverture());
                try { album.setCouverture(fileService.store(couverture, "albums")); }
                catch (IOException e) { throw new RuntimeException(e); }
            }
            return ResponseEntity.ok(albumRepo.save(album));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return albumRepo.findById(id).map(album -> {
            fileService.delete(album.getCouverture());
            album.getPhotos().forEach(p -> fileService.delete(p.getSrc()));
            albumRepo.delete(album);
            return ResponseEntity.ok().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }

    // ─── Photos d'un album ───

    @PostMapping("/{id}/photos")
    public ResponseEntity<List<Photo>> addPhotos(
            @PathVariable Long id,
            @RequestParam("files") MultipartFile[] files) throws IOException {

        return albumRepo.findById(id).map(album -> {
            List<Photo> added = new ArrayList<>();
            for (MultipartFile file : files) {
                try {
                    String src = fileService.store(file, "albums/" + id);
                    Photo photo = Photo.builder()
                            .src(src)
                            .alt(album.getTitre())
                            .album(album)
                            .build();
                    added.add(photoRepo.save(photo));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return ResponseEntity.ok(added);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{albumId}/photos/{photoId}")
    public ResponseEntity<Void> deletePhoto(@PathVariable Long albumId, @PathVariable Long photoId) {
        return photoRepo.findById(photoId).map(photo -> {
            fileService.delete(photo.getSrc());
            photoRepo.delete(photo);
            return ResponseEntity.ok().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
