package com.cac.backend.controller;

import com.cac.backend.service.EmailService;
import com.cac.backend.dto.AuthDto.*;
import com.cac.backend.entity.AdminUser;
import com.cac.backend.repository.AdminUserRepository;
import com.cac.backend.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AdminUserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

    // ─── LOGIN ───
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        var userOpt = userRepo.findByEmail(request.getEmail());

        if (userOpt.isEmpty() || !passwordEncoder.matches(request.getPassword(), userOpt.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("Email ou mot de passe incorrect"));
        }

        AdminUser user = userOpt.get();
        String token = jwtUtil.generateToken(user.getEmail());
        return ResponseEntity.ok(new LoginResponse(token, user.getEmail(), user.getNom()));
    }

    // ─── GET CURRENT USER ───
    @GetMapping("/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal AdminUser user) {
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return ResponseEntity.ok(Map.of(
            "email", user.getEmail(),
            "nom", user.getNom() != null ? user.getNom() : ""
        ));
    }

    // ─── FORGOT PASSWORD: envoie un code par mail ───
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        return userRepo.findByEmail(request.getEmail())
            .map(user -> {
                // Générer un code à 6 chiffres
                String code = String.format("%06d", new Random().nextInt(999999));
                user.setResetCode(code);
                user.setResetCodeExpiry(LocalDateTime.now().plusMinutes(15));
                userRepo.save(user);

                // Envoyer par mail
                try {
                    emailService.sendResetCode(user.getEmail(), code);
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new MessageResponse("Erreur lors de l'envoi de l'email"));
                }

                return ResponseEntity.ok(new MessageResponse("Code envoyé à " + user.getEmail()));
            })
            .orElse(ResponseEntity.ok(new MessageResponse("Si cet email existe, un code a été envoyé")));
    }

    // ─── VERIFY CODE ───
    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCode(@RequestBody VerifyCodeRequest request) {
        return userRepo.findByEmail(request.getEmail())
            .filter(user -> user.getResetCode() != null
                && user.getResetCode().equals(request.getCode())
                && user.getResetCodeExpiry() != null
                && user.getResetCodeExpiry().isAfter(LocalDateTime.now()))
            .map(user -> ResponseEntity.ok(new MessageResponse("Code valide")))
            .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MessageResponse("Code invalide ou expiré")));
    }

    // ─── RESET PASSWORD (avec code) ───
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        return userRepo.findByEmail(request.getEmail())
            .filter(user -> user.getResetCode() != null
                && user.getResetCode().equals(request.getCode())
                && user.getResetCodeExpiry() != null
                && user.getResetCodeExpiry().isAfter(LocalDateTime.now()))
            .map(user -> {
                user.setPassword(passwordEncoder.encode(request.getNewPassword()));
                user.setResetCode(null);
                user.setResetCodeExpiry(null);
                userRepo.save(user);
                return ResponseEntity.ok(new MessageResponse("Mot de passe réinitialisé avec succès"));
            })
            .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MessageResponse("Code invalide ou expiré")));
    }

    // ─── CHANGE PASSWORD (connecté) ───
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @AuthenticationPrincipal AdminUser user,
            @RequestBody ChangePasswordRequest request) {

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MessageResponse("Mot de passe actuel incorrect"));
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepo.save(user);

        return ResponseEntity.ok(new MessageResponse("Mot de passe modifié avec succès"));
    }
}
