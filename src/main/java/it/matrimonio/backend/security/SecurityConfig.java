package it.matrimonio.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendError(401, "Unauthorized")
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        // =========================
                        // API PUBBLICHE
                        // =========================

                        // Login degli sposi
                        .requestMatchers("/api/auth/**").permitAll()

                        // Creazione partecipante
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/guests"
                        ).permitAll()

                        // RSVP partecipante
                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/guests/*/rsvp"
                        ).permitAll()

                        // Creazione companion
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/guests/*/companions"
                        ).permitAll()


                        // =========================
                        // API PRIVATE - ADMIN
                        // =========================

                        // Guest
                        .requestMatchers("/api/guests/**")
                        .hasRole("ADMIN")

                        // Companion
                        .requestMatchers("/api/companions/**")
                        .hasRole("ADMIN")

                        // Statistiche
                        .requestMatchers("/api/stats/**")
                        .hasRole("ADMIN")


                        // =========================
                        // DEFAULT
                        // =========================

                        .anyRequest().authenticated()
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}