package com.amrit.jwt.config;

import com.amrit.jwt.exception.CustomAccessDeniedHandler;
import com.amrit.jwt.service.impl.AppUserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final AppUserServiceImpl appUserServiceImpl;
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomLogoutHandler customLogoutHandler;

    public SecurityConfig(AppUserServiceImpl appUserServiceImpl, JwtAuthenticationFilter jwtAuthFilter, CustomAccessDeniedHandler customAccessDeniedHandler, CustomLogoutHandler customLogoutHandler) {
        this.appUserServiceImpl = appUserServiceImpl;
        this.jwtAuthFilter = jwtAuthFilter;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
        this.customLogoutHandler = customLogoutHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        req -> req.requestMatchers("/login/**", "/register/**", "/refreshToken/**")
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .userDetailsService(appUserServiceImpl)
                .exceptionHandling(e->e
                        .accessDeniedHandler(customAccessDeniedHandler))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(l->l.logoutUrl("/logout")
                        .addLogoutHandler(customLogoutHandler)
                        .logoutSuccessHandler(
                                (request, response, authentication) -> SecurityContextHolder.clearContext()
                        ))
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
