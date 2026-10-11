package com.nsoft.sliit.videobrowse.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService uds, PasswordEncoder encoder){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(uds);
        provider.setPasswordEncoder(encoder);
        return provider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, RedirectAfterLogin successHandler, UserDetailsService uds) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login", "/register", "/favicon.ico", "/css/**", "/js/**", "/img/**", "/files/videos/**", "/files/thumbs/**", "/h2/**").permitAll()
                .requestMatchers("/profile/**").authenticated()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/support/new").hasRole("USER") // Only regular users can create tickets
                .requestMatchers("/support/my", "/support/*/edit", "/support/*/delete").hasRole("USER") // User self-service
                .requestMatchers("/support/list", "/support/*/status", "/support/*/note").hasRole("ADMIN") // Admin management
                .anyRequest().authenticated()
            )
            .formLogin(login -> login
                .loginPage("/login")
                .successHandler(successHandler)
                .permitAll()
            )
            .rememberMe(rm -> rm
                .key("videobrowse-remember-me-key")
                .rememberMeParameter("remember-me")
                .userDetailsService(uds)
                .tokenValiditySeconds(60 * 60 * 24 * 14) // 14 days
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll()
            );
        return http.build();
    }

    // Removed persistent remember-me repository for simpler, stable config
}
