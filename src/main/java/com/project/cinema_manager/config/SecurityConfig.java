package com.project.cinema_manager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

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
                http.csrf(csrf -> csrf.disable()) // Mantengo la disattivazione CSRF per i tuoi test
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/admin/**").hasAuthority("ADMIN") // Come da slide
                                                .requestMatchers("/", "/api/**", "/css/**", "/js/**").permitAll()
                                                .anyRequest().authenticated())
                                .formLogin(form -> form.permitAll())
                                .logout(logout -> logout.permitAll());

                // Colleghiamo l'authentication provider
                http.authenticationProvider(authenticationProvider());

                return http.build();
        }
}