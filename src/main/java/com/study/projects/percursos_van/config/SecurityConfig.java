package com.study.projects.percursos_van.config;

import com.study.projects.percursos_van.jwt.JwtEntryPoint;
import com.study.projects.percursos_van.jwt.JwtRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableMethodSecurity
@EnableWebMvc
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        return security
                .httpBasic(x -> x.disable())
                .formLogin(x -> x.disable())
                .csrf(x -> x.ignoringRequestMatchers("/h2/**").disable())
                .headers(x -> x.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .exceptionHandling(x -> x.authenticationEntryPoint(new JwtEntryPoint()))
                .authorizeHttpRequests(x -> x.requestMatchers(
                                antMatcher(HttpMethod.POST, "/api/v1/auth"),
                                antMatcher(HttpMethod.GET, "/api/v1/confirmation"),
                                antMatcher("/h2/**"))
                        .permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(x -> x.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(filter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtRequestFilter filter() {
        return new JwtRequestFilter();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration a) throws Exception {
        return a.getAuthenticationManager();
    }
}
