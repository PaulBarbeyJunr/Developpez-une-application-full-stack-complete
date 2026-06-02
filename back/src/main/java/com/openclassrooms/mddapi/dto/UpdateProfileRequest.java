package com.openclassrooms.mddapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Données de mise à jour partielle du profil utilisateur.
 * Tous les champs sont optionnels : seuls ceux qui sont non null
 * et non vides sont effectivement appliqués.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequest {

    @Size(min = 3, max = 50)
    private String username;

    @Email
    @Size(max = 100)
    private String email;

    /**
     * Nouveau mot de passe (optionnel).
     * Si fourni, doit respecter les règles : 8 caractères minimum,
     * au moins une majuscule, une minuscule, un chiffre et un caractère spécial.
     */
    @Pattern(
            regexp = "^$|^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$",
            message = "Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule, un chiffre et un caractère spécial"
    )
    private String password;
}
