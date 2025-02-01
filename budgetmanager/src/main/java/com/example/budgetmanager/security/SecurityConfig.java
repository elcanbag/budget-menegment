package com.example.budgetmanager.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF korumasını devre dışı bırak
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/signup", "/auth/login", "/h2-console/**").permitAll() // No Auth gereken yollar
                        .anyRequest().authenticated() // Diğer tüm yollar için Basic Auth zorunlu
                )
                .httpBasic(); // Basic Auth etkinleştir

        // H2 Console'un çalışabilmesi için FrameOptions'u devre dışı bırak
        http.headers(headers -> headers.frameOptions().disable());

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance()); // Şifre düz metin olarak saklanıyor
        return new ProviderManager(authProvider);
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        // Test için NoOp (şifreleri açık metin olarak tutar, gerçek projelerde kullanılmaz)
        return NoOpPasswordEncoder.getInstance();
    }


}
