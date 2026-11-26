
package tn.iset.gestion_formation.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
            .authorizeHttpRequests(auth -> auth

                // Pages publiques
                .requestMatchers(
                    "/", "/login", "/403",
                    "/css/**", "/js/**", "/images/**",
                    "/h2-console/**"
                ).permitAll()

                // API REST : lecture pour USER et ADMIN
                .requestMatchers(HttpMethod.GET, "/api/**")
                    .hasAnyRole("USER", "ADMIN")

                // API REST : écriture réservée à ADMIN
                .requestMatchers(HttpMethod.POST, "/api/**")
                    .hasRole("ADMIN")

                .requestMatchers(HttpMethod.PUT, "/api/**")
                    .hasRole("ADMIN")

                .requestMatchers(HttpMethod.DELETE, "/api/**")
                    .hasRole("ADMIN")

                // Pages MVC réservées à ADMIN
                .requestMatchers(
                    "/sessions/nouveau",
                    "/formateurs/nouveau",
                    "/formateurs/modifier/**",
                    "/apprenants/nouveau",
                    "/inscriptions/nouveau"
                ).hasRole("ADMIN")

                // Enregistrement MVC réservé à ADMIN
                .requestMatchers(
                    HttpMethod.POST,
                    "/sessions/enregistrer",
                    "/formateurs/enregistrer",
                    "/apprenants/enregistrer",
                    "/inscriptions/enregistrer",
                    "/formateurs/supprimer/**"
                ).hasRole("ADMIN")

                // Autres pages : connexion obligatoire
                .anyRequest().authenticated()
            )

            .formLogin(Customizer.withDefaults())
            .httpBasic(Customizer.withDefaults())

            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )

            .exceptionHandling(exception -> exception
                .accessDeniedPage("/403")
            )

            // On conserve la protection CSRF
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**")
            )

            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin())
            );

        return http.build();
    }
}
