package com.vdska.pointsapi2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .cors(cors -> {})
//                .sessionManagement(session ->
//                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                )
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/v1/auth/**").permitAll()
//                        .anyRequest().authenticated()
//                );
//
//        return http.build();
//    }
//    @Bean
//    public UserDetailsService userDetailsService(DataSource dataSource) {
//        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
//
//        userDetailsManager.setUsersByUsernameQuery(
//                "select username, password, enabled " +
//                        "from users where username = ?"
//        );
//
//        userDetailsManager.setAuthoritiesByUsernameQuery(
//                "select username, authority " +
//                        "from authorities where username = ?"
//        );
//
//        return userDetailsManager;
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
        return config.getAuthenticationManager();
    }
}