package com.study.projects.percursos_van.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class JwtEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setHeader("Authentication", "bearer realm='/api/v1/login'");
        response.setContentType("application/json");
        response.setStatus(401);
        String token = request.getHeader("Authorization");
        if(token == null){
            response.getWriter().write("{\"error\":\"Você precisa estar autenticado para acessar este recurso\"}");
        }else{
            response.getWriter().write("{\"error\":\"O token informado é inválido ou está expirado\"}");
        }
    }
}
