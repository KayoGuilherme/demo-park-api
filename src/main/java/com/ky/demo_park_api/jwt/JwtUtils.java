package com.ky.demo_park_api.jwt;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtUtils {

    public static final String JWT_BEARER = "Bearer ";

    public static final String JWT_AUTHORIZATION = "Authorization";

    public static final String SECRET_KEY = "asdfghjklq-1234567892-a1s2d3f4g53388";

    public static final long EXPIRE_DAYS = 1;

    public static final long EXPIRE_HOURS = 0;

    public static final long EXPIRE_MINUTES = 0;

    private JwtUtils() {

    }

    private static javax.crypto.SecretKey generateKey() {
        // Cria uma chave HMAC a partir da SECRET_KEY
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    // Converte a data de início para a data de expiração adicionando o tempo configurado
    private static Date toExpireDate(Date start) {
        // Converte a data de início para LocalDateTime
        LocalDateTime dateTime = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        // Adiciona o tempo configurado à data de início
        LocalDateTime end = dateTime.plusDays(EXPIRE_DAYS).plusHours(EXPIRE_HOURS).plusMinutes(EXPIRE_MINUTES);
        // Converte a data final de volta para Date
        return Date.from(end.atZone(ZoneId.systemDefault()).toInstant());
    }

  
    public static JwtToken createToken(String email, String role) {
        Date issuedAt = new Date(); // Data atual como data de emissão
        Date limit = toExpireDate(issuedAt); // Calcula a data de expiração
        // Constrói o token JWT
        String token = Jwts.builder()
                .header().add("typ", "JWT") // Define o tipo de token no cabeçalho
                .and()
                .subject(email) // Define o e-mail como o assunto do token
                .issuedAt(issuedAt) // Define a data de emissão
                .expiration(limit) // Define a data de expiração
                .signWith(generateKey()) // Assina o token com a chave secreta gerada
                .claim("role", role) // Adiciona o papel (role) como um claim no token
                .compact(); // Compacta o JWT para uma string
        return new JwtToken(token); // Retorna um objeto JwtToken contendo o JWT gerado
    }

   
    private static Claims getClaimsFromToken(String token) {
        try {
          
            return Jwts.parser()
                    .verifyWith(generateKey()) // Define a chave para verificar a assinatura
                    .build()
                    .parseSignedClaims((CharSequence) refactorToken(token)).getPayload(); // Obtém os claims do token
        } catch (JwtException ex) {
           
            log.error(String.format("Token inválido %s", ex.getMessage()));
        }
        return null;
    }

    // Obtém o e-mail dos claims do token JWT
    public static String getEmailFromToken(String token) {
        // Obtém o e-mail (subject) do token JWT
        return getClaimsFromToken(token).getSubject();
    }

    // Remove o prefixo "bearer " do token, se presente
    private static Object refactorToken(String token) {
        if (token.contains(JWT_BEARER)) {
            // Remove o prefixo "bearer " do token
            return token.substring(JWT_BEARER.length());
        }
        return token; // Retorna o token original se o prefixo não estiver presente
    }

  
    public static boolean isTokenValid(String token) {
        try {
          
            Jwts.parser()
                    .verifyWith(generateKey()) // Define a chave para verificar a assinatura
                    .build()
                    .parseSignedClaims((CharSequence) refactorToken(token)); // Analisa o token JWT
            return true; 
        } catch (JwtException ex) {
          
            log.error(String.format("Token inválido %s", ex.getMessage()));
        }
        return false; 
    }

}
