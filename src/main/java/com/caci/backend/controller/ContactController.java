package com.caci.backend.controller;

import com.caci.backend.dto.AuthDto.MessageResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    private static final String DEST_EMAIL = "contact@courbevoie-athletisme.fr";

    @PostMapping
    public ResponseEntity<?> sendContact(@RequestBody ContactForm form) {
        // Validation
        if (form.getNom() == null || form.getNom().isBlank()
            || form.getPrenom() == null || form.getPrenom().isBlank()
            || form.getEmail() == null || form.getEmail().isBlank()
            || form.getMessage() == null || form.getMessage().isBlank()) {
            return ResponseEntity.badRequest()
                .body(new MessageResponse("Veuillez remplir tous les champs obligatoires."));
        }

        // Construire le mail
        String subject = form.getObjet() != null && !form.getObjet().isBlank()
            ? "[Contact Site] " + form.getObjet()
            : "[Contact Site] Message de " + form.getPrenom() + " " + form.getNom();

        String body = "Nouveau message depuis le formulaire de contact\n"
            + "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n"
            + "Nom : " + form.getNom() + "\n"
            + "Prénom : " + form.getPrenom() + "\n"
            + "Email : " + form.getEmail() + "\n"
            + "Objet : " + (form.getObjet() != null ? form.getObjet() : "—") + "\n\n"
            + "Message :\n" + form.getMessage() + "\n\n"
            + "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n"
            + "Envoyé depuis le site Club Athlétisme Courbevoie";

        // Afficher dans la console
        System.out.println("\n📩 NOUVEAU MESSAGE DE CONTACT");
        System.out.println(body);

        // Envoyer par mail si configuré
        if (mailSender != null) {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(DEST_EMAIL);
                message.setReplyTo(form.getEmail());
                message.setSubject(subject);
                message.setText(body);
                mailSender.send(message);
            } catch (Exception e) {
                System.out.println("⚠ Email non envoyé : " + e.getMessage());
            }
        }

        return ResponseEntity.ok(new MessageResponse("Message envoyé avec succès."));
    }

    @Data
    public static class ContactForm {
        private String nom;
        private String prenom;
        private String email;
        private String objet;
        private String message;
    }
}
