package com.study.projects.percursos_van.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Component
public class JwtUtils {

    protected final String authorization = "Authorization";
    protected final String bearer = "Bearer ";
    private final int durationInHours = 2;

    @Value("${jwt.signature}")
    private String signature;

    private SecretKey generateSecretKey(){
        return Keys.hmacShaKeyFor(signature.getBytes(StandardCharsets.UTF_8));
    }

    private Date getExpiration(Date start){
        LocalDateTime dt = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        dt = dt.plusHours(durationInHours);
        return Date.from(dt.atZone(ZoneId.systemDefault()).toInstant());
    }

    public JwtToken generateToken(String username, String role){
        Date now = new Date();
        Date exp = getExpiration(now);
        try {
            String token = Jwts.builder()
                    .signWith(generateSecretKey())
                    .subject(username)
                    .issuedAt(now)
                    .expiration(exp)
                    .claim("role", role)
                    .header().add("typ", "JWT").and()
                    .compact();
            return new JwtToken(token);
        }catch(JwtException e){
            log.info("Falha na geração do token ", e);
        }
        return null;
    }

    private Claims getClaimsFromToken(String token){
        try{
            return Jwts.parser().verifyWith(generateSecretKey())
                    .build().parseSignedClaims(removeBearer(token)).getPayload();
        }catch(JwtException e){
            log.info("Falha na extração do payload do token: ", e);
        }
        return null;
    }

    private String removeBearer(String token){
        if( token != null && token.startsWith(bearer)){
            return token.substring(bearer.length());
        }
        return token;
    }

    public String getSubject(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims != null) {
            return claims.getSubject();
        }
        return "Token está nulo";
    }

    public boolean isTokenValid(String token){
        try{
            Jwts.parser().verifyWith(generateSecretKey())
                    .build()
                    .parseSignedClaims(removeBearer(token));
            return true;
        }catch(JwtException e){
            log.info("Token inválido ou expirado: ", e);
        }
        return false;
    }

}
