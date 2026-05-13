package com.cac.backend.config;

import com.cac.backend.entity.AdminUser;
import com.cac.backend.entity.SiteSetting;
import com.cac.backend.repository.AdminUserRepository;
import com.cac.backend.repository.SiteSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInit implements CommandLineRunner {

    private final AdminUserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final SiteSettingRepository settingRepo;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        if (userRepo.count() == 0) {
            AdminUser admin = AdminUser.builder()
                    .email(adminEmail)
                    .password(passwordEncoder.encode(adminPassword))
                    .nom("Administrateur")
                    .build();
            userRepo.save(admin);
            System.out.println("✅ Admin créé: " + adminEmail + " / " + adminPassword);
        }

        // Créer la saison par défaut
        if (settingRepo.findByCle("saison").isEmpty()) {
            settingRepo.save(SiteSetting.builder()
                    .cle("saison")
                    .valeur("25/26")
                    .build());
            System.out.println("✅ Saison par défaut créée: 25/26");
        }

        // Créer la note tarifs par défaut
        if (settingRepo.findByCle("tarifs_note").isEmpty()) {
            settingRepo.save(SiteSetting.builder()
                    .cle("tarifs_note")
                    .valeur("Une plus-value de 20 € est demandée pour les non résidents à Courbevoie.")
                    .build());
        }
    }
}
