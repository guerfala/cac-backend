package com.cac.backend.config;

import com.cac.backend.entity.AdminUser;
import com.cac.backend.repository.AdminUserRepository;
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
    }
}
