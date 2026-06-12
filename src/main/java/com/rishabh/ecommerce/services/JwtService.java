package com.rishabh.ecommerce.services;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service

public class JwtService {

    @Value("${jwt.secret}")
    private String secretString;

    //its main job is to create the key
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode((secretString));
        return Keys.hmacShaKeyFor(keyBytes);
    }


    //key will be used here for generating token(main use of compact())
    public String generateToken(String username) {
        return Jwts.builder()
        .subject(username)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 6))
        .signWith(getSigningKey())
        .compact();

    }

    //When a request comes in with a token, they crack the token open to read the data inside.

}
