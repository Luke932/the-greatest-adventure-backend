package it.matrimonio.backend.config;

import it.matrimonio.backend.model.Role;
import it.matrimonio.backend.model.User;
import it.matrimonio.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initAdmin() {
        return args -> {

            if (!userRepository.existsByEmail("sposi@email.com")) {

                User user = User.builder()
                        .email("sposi@email.com")
                        .password(passwordEncoder.encode("password123"))
                        .role(Role.ADMIN)
                        .build();

                userRepository.save(user);
            }
        };
    }
}