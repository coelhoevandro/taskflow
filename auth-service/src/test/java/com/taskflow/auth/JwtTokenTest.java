package com.taskflow.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenTest {

    private final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    @Test
    void shouldGenerateValidJwtToken() {
        String username = "evandro";

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(key)
                .compact();

        assertNotNull(token);
        assertTrue(token.length() > 10);

        String parsedSubject = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        assertEquals(username, parsedSubject);
    }
}

