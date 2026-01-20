package com.espe.test.test.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((oauthHttp)-> oauthHttp
                        // 1. QUITA el permitAll() de /authorized. Debe estar protegida para que se dispare el login.
                        .requestMatchers(HttpMethod.GET, "/api/libros/**").hasAuthority("SCOPE_read")
                        .requestMatchers(HttpMethod.POST, "/api/libros/**").hasAuthority("SCOPE_write")
                        .requestMatchers(HttpMethod.PUT, "/api/libros/**").hasAuthority("SCOPE_write")
                        .requestMatchers(HttpMethod.DELETE, "/api/libros/**").hasAuthority("SCOPE_write")
                        // 2. Deja que cualquier otra ruta (incluyendo /authorized) requiera autenticación
                        .anyRequest().authenticated())
                // 3. CAMBIA a SessionCreationPolicy.ALWAYS o IF_REQUIRED.
                // OAuth2 Login por navegador NECESITA sesión para guardar el estado entre redirecciones.
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .oauth2Login((login) -> login
                        .loginPage("/oauth2/authorization/libro-ms")
                        .defaultSuccessUrl("/authorized", true)
                )
                .oauth2Client(Customizer.withDefaults())
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(Customizer.withDefaults())
                );
        return http.build();
    }
}