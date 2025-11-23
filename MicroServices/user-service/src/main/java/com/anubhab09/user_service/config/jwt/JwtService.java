package com.anubhab09.user_service.config.jwt;

import com.anubhab09.user_service.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.security.Key;
import java.util.Date;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String jwtSecert;

    @Value("${jwt.expiration-ms}")
    private Long expirationMs;

    private Key getSignKey(){
        return Keys.hmacShaKeyFor(jwtSecert.getBytes());
    }

    public String generateToken(User user){
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("role", user.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token){
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, User user){
        String username = extractUsername(token);
        return username.equals(user.getUsername()) && !isToeknExpired(token);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isToeknExpired(String token){
        return extractAllClaims(token).getExpiration().before(new Date());
    }

}
