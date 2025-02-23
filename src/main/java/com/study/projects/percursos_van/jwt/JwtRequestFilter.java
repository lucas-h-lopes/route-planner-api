package com.study.projects.percursos_van.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUserDetailsService service;

    @Autowired
    private  JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(jwtUtils.authorization);
        String path = request.getRequestURI();
        if(token == null || !token.startsWith(jwtUtils.bearer)){
            log.info("Token nulo ou não inicializado com 'Bearer '");
            filterChain.doFilter(request, response);
            return;
        }
        if(!jwtUtils.isTokenValid(token)){
            filterChain.doFilter(request, response);
            return;
        }

        String username = jwtUtils.getSubject(token);
        authenticate(username, request);
        filterChain.doFilter(request, response);
    }

    private void authenticate(String username, HttpServletRequest request){
        UserDetails userDetails = service.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken token =
                UsernamePasswordAuthenticationToken.authenticated(username, null, userDetails.getAuthorities());
        token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(token);
    }
}
