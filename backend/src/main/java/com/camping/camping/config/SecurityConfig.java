package com.camping.camping.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // On désactive CSRF pour les appels API (sinon 403 en POST/PUT sans token)
                .csrf(AbstractHttpConfigurer::disable)

                // Active CORS (utilise ta CorsConfig)
                .cors(Customizer.withDefaults())

                // En prod, autorise les iframes de même origine (utile si tu utilises H2 console)
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))

                // Règles d'autorisation
                .authorizeHttpRequests(auth -> auth
                        // Autorise l'API publique
                        .requestMatchers("/api/**").permitAll()

                        // (Optionnel) Actuator/Health
                        .requestMatchers("/actuator/**").permitAll()

                        // (Optionnel) H2 console si tu l’utilises en dev
                        .requestMatchers("/h2-console/**").permitAll()

                        // (Optionnel) Fichiers statiques, page d’accueil, erreurs
                        .requestMatchers("/", "/index.html", "/favicon.ico", "/error").permitAll()

                        // Tout le reste : autorisé (si tu n'as pas d'auth pour le moment)
                        .anyRequest().permitAll()
                );

        // Si un jour tu veux forcer une auth basique :
        // .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
