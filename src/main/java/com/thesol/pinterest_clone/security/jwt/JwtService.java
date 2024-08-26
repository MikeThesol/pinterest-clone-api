package com.thesol.pinterest_clone.security;

import com.thesol.pinterest_clone.dto.JwtAuthenticationDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
@Slf4j
public class JwtService {

    @Value("8e436a43545c96286e401a8c12c38123a403a755761d1795f95e8e87791321403812ab218421ade53ae99b19e43ed6368922979bf622f189fcd2b63aacd1c5d72265d0941b9c739f8cdf9b10dc424d61347ac171be45348c3a6758a3afb85f06ed0017e673e6e50313182c67cd6dccff423ccc832cd11fcacba7786ec83fbc4eba1ed934f7dfd6dd7efe19be3d77de0a9f28228d5e287354ba2cb7971aea7f8e8dee6e4ffcec4998e07a03428d348feb0b99e3619a3117be804199b00c999b741d1d449b7e254fba9f73876e94c7fe516c41c158a12e13fbe1a50c2a8bd9cdea1b7f509508209db4fd19363898303443f8664b6f6d2fe8a83d97f43097c550a7")
    private String jwtSecret;

    public JwtAuthenticationDto generateJwtAuthToken(String email) {
        JwtAuthenticationDto jwtDto = new JwtAuthenticationDto();
        jwtDto.setToken(generateJwtToken(email));
        jwtDto.setRefreshToken(generateRefreshToken(email));
        return jwtDto;
    }

    public JwtAuthenticationDto refreshBaseToken(String email, String refreshToken) {
        JwtAuthenticationDto jwtDto = new JwtAuthenticationDto();
        jwtDto.setToken(generateJwtToken(email));
        jwtDto.setRefreshToken(refreshToken);
        return jwtDto;
    }

    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return true;
        } catch (ExpiredJwtException e) {
            log.error("Expired JwtException", e);
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JwtException", e);
        } catch (MalformedJwtException e) {
            log.error("Malformed JwtException", e);
        } catch (SecurityException e) {
            log.error("Security exception", e);
        } catch (Exception e) {
            log.error("Invalid token", e);
        }
        return false;
    }

    private String generateJwtToken(String email) {
        Date date = Date.from(LocalDateTime.now().plusHours(1).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .subject(email)
                .expiration(date)
                .signWith(getSignInKey())
                .compact();
    }

    private String generateRefreshToken(String email) {
        Date date = Date.from(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .subject(email)
                .expiration(date)
                .signWith(getSignInKey())
                .compact();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
