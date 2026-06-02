package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.TopicDto;
import com.openclassrooms.mddapi.dto.UpdateProfileRequest;
import com.openclassrooms.mddapi.dto.UserProfileDto;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service métier de gestion du profil utilisateur connecté :
 * consultation et mise à jour partielle (username, email, mot de passe).
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Renvoie le profil complet de l'utilisateur courant, abonnements inclus.
     *
     * @return le {@link UserProfileDto} de l'utilisateur authentifié
     */
    @Transactional(readOnly = true)
    public UserProfileDto getCurrentProfile() {
        User user = loadCurrentUser();
        return toProfileDto(user);
    }

    /**
     * Met à jour partiellement le profil de l'utilisateur courant.
     * Seuls les champs renseignés (non null et non vides) sont appliqués.
     * Le mot de passe, s'il est fourni, est haché via BCrypt.
     *
     * @param request les nouvelles valeurs souhaitées
     * @return le profil mis à jour
     * @throws IllegalArgumentException si l'email/username demandé est déjà pris par un autre compte
     */
    @Transactional
    public UserProfileDto updateProfile(UpdateProfileRequest request) {
        User user = loadCurrentUser();

        if (StringUtils.hasText(request.getUsername())
                && !request.getUsername().equals(user.getUsername())) {
            if (userRepository.existsByUsername(request.getUsername())) {
                throw new IllegalArgumentException("Ce nom d'utilisateur est déjà utilisé");
            }
            user.setUsername(request.getUsername());
        }

        if (StringUtils.hasText(request.getEmail())
                && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new IllegalArgumentException("Cet email est déjà utilisé");
            }
            user.setEmail(request.getEmail());
        }

        if (StringUtils.hasText(request.getPassword())) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        User saved = userRepository.save(user);
        return toProfileDto(saved);
    }

    private User loadCurrentUser() {
        Long id = SecurityUtils.getCurrentUserId();
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable"));
    }

    private UserProfileDto toProfileDto(User user) {
        List<TopicDto> subscriptions = user.getSubscriptions().stream()
                .map(this::toTopicDto)
                .collect(Collectors.toList());

        return UserProfileDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .subscriptions(subscriptions)
                .build();
    }

    private TopicDto toTopicDto(Topic topic) {
        return TopicDto.builder()
                .id(topic.getId())
                .title(topic.getTitle())
                .description(topic.getDescription())
                .build();
    }
}
