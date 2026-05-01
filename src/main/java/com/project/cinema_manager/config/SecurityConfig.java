package com.project.cinema_manager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                // 1. Configurazione permessi con Lambda
                                .authorizeHttpRequests(requests -> requests
                                                .requestMatchers("/admin/**").hasRole("ADMIN") // Solo admin nel
                                                                                               // backoffice
                                                .requestMatchers("/", "/api/**", "/css/**", "/js/**").permitAll() // API
                                                                                                                  // e
                                                                                                                  // asset
                                                                                                                  // liberi
                                                .anyRequest().authenticated())
                                // 2. Form di Login con default (utilizza la pagina di default di Spring)
                                .formLogin(Customizer.withDefaults())
                                // 3. Logout
                                .logout(logout -> logout
                                                .logoutSuccessUrl("/")
                                                .permitAll())
                                // 4. Disattivazione CORS e CSRF (essenziale per la futura integrazione React)
                                .cors(cors -> cors.disable())
                                .csrf(csrf -> csrf.disable());

                return http.build();
        }

        @Bean
        public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
                DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
                authProvider.setPasswordEncoder(passwordEncoder());
                return authProvider;
        }

        /*
         * @Bean
         * public UserDetailsService userDetailsService() {
         * UserDetails admin = User.builder()
         * .username("admin")
         * .password(passwordEncoder().encode("password"))
         * .roles("ADMIN")
         * .build();
         * return new InMemoryUserDetailsManager(admin);
         * }
         */

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}