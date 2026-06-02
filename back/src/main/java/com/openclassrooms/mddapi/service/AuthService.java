package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.auth.AuthResponse;
import com.openclassrooms.mddapi.dto.auth.LoginRequest;
import com.openclassrooms.mddapi.dto.auth.RegisterRequest;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.security.JwtUtils;
import com.openclassrooms.mddapi.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service métier d'authentification : inscription et connexion.
 * Encapsule la logique de hachage des mots de passe (BCrypt) et
 * de génération des JWT, conformément aux principes SOLID
 * (séparation des responsabilités).
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private static final String TOKEN_TYPE = "Bearer";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    /**
     * Inscrit un nouvel utilisateur après vérification de l'unicité
     * de son email et de son nom d'utilisateur. Le mot de passe est
     * haché via BCrypt avant persistance.
     *
     * @param request les données d'inscription validées
     * @return un {@link AuthResponse} contenant le JWT et les infos utilisateur
     * @throws IllegalArgumentException si l'email ou le nom d'utilisateur est déjà utilisé
     */
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Cet email est déjà utilisé");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Ce nom d'utilisateur est déjà utilisé");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        User saved = userRepository.save(user);

        return authenticate(saved.getUsername(), request.getPassword());
    }

    /**
     * Authentifie un utilisateur via email OU nom d'utilisateur.
     *
     * @param request les identifiants fournis
     * @return un {@link AuthResponse} contenant le JWT et les infos utilisateur
     */
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        return authenticate(request.getIdentifier(), request.getPassword());
    }

    private AuthResponse authenticate(String identifier, String rawPassword) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(identifier, rawPassword));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtUtils.generateJwtToken(userDetails);

        return AuthResponse.builder()
                .token(jwt)
                .type(TOKEN_TYPE)
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .build();
    }
}
