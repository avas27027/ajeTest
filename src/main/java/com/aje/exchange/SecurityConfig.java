package com.aje.exchange;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Value("${app.username}")
    private String username;

    @Value("${app.password}")
    private String password;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(request -> {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOrigins(Arrays.asList("*"));
            configuration.setAllowedMethods(Arrays.asList("*"));
            configuration.setAllowedHeaders(Arrays.asList("*"));
            return configuration;
        }));
        http.securityMatcher("/home/**", "/exchange/**")
                .authorizeHttpRequests(request -> request.requestMatchers("/home/**").permitAll()
                        .requestMatchers("/exchange/**").authenticated())
                .httpBasic(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable());
        return http.build();
    }

    @Bean
    public UserDetailsService adminUser(PasswordEncoder passwordEncoder) {
        User.UserBuilder user = User.builder();
        UserDetails user1 = user.username(username)
                .password(passwordEncoder.encode(password))
                .roles()
                .build();
        return new InMemoryUserDetailsManager(user1);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
