package com.project.cinema_manager.config;

import java.util.Arrays;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Bean
        public DatabaseUserDetailsService userDetailsService() {
                return new DatabaseUserDetailsService();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        }

        @Bean
        public DaoAuthenticationProvider authenticationProvider() {
                // Nuova Sintassi: UserDetailsService passato direttamente nel costruttore
                DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService());
                authProvider.setPasswordEncoder(passwordEncoder());
                return authProvider;
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http.cors(cors -> cors.configurationSource(corsConfigurationSource())) // Abilita e configura CORS
                                .csrf(csrf -> csrf.disable()) // Mantengo la disattivazione CSRF per i tuoi test
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/admin/**").hasAuthority("ADMIN") // Come da slide
                                                .requestMatchers("/", "/api/**", "/css/**", "/js/**", "/error")
                                                .permitAll()
                                                .anyRequest().authenticated())
                                .formLogin(form -> form.permitAll())
                                .logout(logout -> logout.permitAll());

                // Colleghiamo l'authentication provider
                http.authenticationProvider(authenticationProvider());

                return http.build();
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();
                // Specifica l'origine del frontend che può fare richieste
                configuration.setAllowedOrigins(List.of("http://localhost:5173"));
                // Specifica i metodi HTTP consentiti (GET, POST, etc.)
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                // Consenti tutti gli header. Per maggiore sicurezza, potresti voler specificare
                // solo quelli necessari.
                configuration.setAllowedHeaders(List.of("*"));
                // Consente al browser di inviare credenziali (es. cookie) con le richieste
                // CORS.
                configuration.setAllowCredentials(true);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                // Applica questa configurazione CORS a tutte le rotte che iniziano con /api/
                source.registerCorsConfiguration("/api/**", configuration);
                return source;
        }
}