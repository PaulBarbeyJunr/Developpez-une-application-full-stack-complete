package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.UpdateProfileRequest;
import com.openclassrooms.mddapi.dto.UserProfileDto;
import com.openclassrooms.mddapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Contrôleur REST pour le profil de l'utilisateur authentifié.
 * Le préfixe {@code /api/me} évite d'exposer l'identifiant numérique
 * (chaque utilisateur ne peut consulter ou modifier que son propre profil).
 */
@RestController
@RequestMapping("/api/me")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Renvoie le profil complet de l'utilisateur connecté (abonnements inclus).
     */
    @GetMapping
    public ResponseEntity<UserProfileDto> getCurrentProfile() {
        return ResponseEntity.ok(userService.getCurrentProfile());
    }

    /**
     * Met à jour partiellement le profil de l'utilisateur connecté.
     */
    @PutMapping
    public ResponseEntity<UserProfileDto> updateCurrentProfile(
            @Valid @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(userService.updateProfile(request));
    }
}
