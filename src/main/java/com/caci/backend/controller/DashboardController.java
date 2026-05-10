package com.caci.backend.controller;

import com.caci.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DirigiantRepository dirigeantRepo;
    private final EntraineurRepository entraineurRepo;
    private final ActualiteRepository actualiteRepo;
    private final AlbumRepository albumRepo;
    private final PhotoRepository photoRepo;
    private final CongeScolaireRepository congeRepo;

    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("dirigeants", dirigeantRepo.count());
        stats.put("entraineurs", entraineurRepo.count());
        stats.put("actualites", actualiteRepo.count());
        stats.put("albums", albumRepo.count());
        stats.put("photos", photoRepo.count());
        stats.put("conges", congeRepo.count());
        return stats;
    }
}
