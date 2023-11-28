package com.cbo.sso.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import java.util.*;
import org.springframework.security.core.GrantedAuthority;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    // Inject the secret key and expiration time from application.properties
    @Value("${application.security.jwt.secret-key}")
    private String secret;

    @Value("${application.security.jwt.expiration}")
    private long expiration;

    // Create a JWT token from a username, date, and authorities
/*    public String createToken(String username, Collection<? extends GrantedAuthority> authorities) {
        // Get the current date
        Date now = new Date();

        // Calculate the expiration date
        Date expiryDate = new Date(now.getTime() + expiration);

        // Create a JWT builder
        JwtBuilder builder = Jwts.builder()
                .setSubject(username) // Set the username as the subject
                .setIssuedAt(now) // Set the issued date
                .setExpiration(expiryDate) // Set the expiration date
                .signWith(SignatureAlgorithm.HS512, secret); // Sign with the secret key and algorithm

        // Add the authorities as a claim
        if (authorities != null && !authorities.isEmpty()) {
            builder.claim("authorities", authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        }

        // Build and return the token
        return builder.compact();
    }*/

    public String createToken(String username ,Collection<? extends GrantedAuthority> authorities) {
        Map<String, Object> claims = new HashMap<>();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
        claims.put("authorities", authorities.stream()
                .filter(grantedAuthority -> grantedAuthority.getAuthority().length() > 3)
                .map(grantedAuthority -> {
                    String authority = grantedAuthority.getAuthority();
                    if (authority.startsWith("ROLE_")) {
                        return authority;
                    } else {
                        return "ROLE_" + authority;
                    }
                })
                .collect(Collectors.toList()));

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

//    private Key getSignKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(secret);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }

    // Validate a JWT token and return true if it is valid, false otherwise
    public boolean validateToken(String token) {
        try {
            // Parse the token and check if it is expired or malformed
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException | MalformedJwtException | SignatureException | UnsupportedJwtException | IllegalArgumentException e) {
            // Log the exception and return false
            return false;
        }
    }

    // Get the username from a JWT token
    public String getUsernameFromToken(String token) {
        // Parse the token and get the subject (username)
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
    public List<GrantedAuthority> getAuthoritiesFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        List<String> roles = claims.get("authorities", List.class);

        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }

        return authorities;
    }
}
