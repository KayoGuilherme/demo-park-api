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

// Anotação do Lombok para log
@Slf4j
public class JwtUtils {

    // Prefixo usado para o token Bearer em cabeçalhos HTTP
    public static final String JWT_BEARER = "bearer ";

    // Nome do cabeçalho HTTP onde o token é esperado
    public static final String JWT_AUTHORIZATION = "Authorization";

    // Chave secreta usada para assinar o JWT
    public static final String SECRET_KEY = "asdfghjklq-1234567892-a1s2d3f4g533";

    // Configurações de expiração do token
    public static final long EXPIRE_DAYS = 0;    // Dias para expiração (zero dias)
    public static final long EXPIRE_HOURS = 0;   // Horas para expiração (zero horas)
    public static final long EXPIRE_MINUTES = 2; // Minutos para expiração (2 minutos)

    // Construtor privado para impedir a instância da classe
    private JwtUtils() {

    }

    // Gera uma chave secreta para assinatura do JWT usando a chave fornecida
    private static javax.crypto.SecretKey generateKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    // Converte a data de início para a data de expiração adicionando dias, horas e minutos
    private static Date toExpireDate(Date start) {
        // Converte a data de início para LocalDateTime
        LocalDateTime dateTime = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        // Adiciona o tempo de expiração
        LocalDateTime end = dateTime.plusDays(EXPIRE_DAYS).plusHours(EXPIRE_HOURS).plusMinutes(EXPIRE_MINUTES);
        // Converte de volta para Date
        return Date.from(end.atZone(ZoneId.systemDefault()).toInstant());
    }

    // Cria um novo token JWT com o e-mail e o papel fornecidos
    public static JwtToken createToken(String email, String role) {
        // Data de emissão do token
        Date issuedAt = new Date();
        // Data de expiração do token
        Date limit = toExpireDate(issuedAt);
        // Cria e assina o token JWT
        String token = Jwts.builder()
                .header().add("typ", "JWT") // Adiciona um cabeçalho indicando o tipo JWT
                .and()
                .subject(email)              // Define o e-mail como o assunto do token
                .issuedAt(issuedAt)          // Define a data de emissão
                .expiration(limit)           // Define a data de expiração
                .signWith(generateKey())     // Assina o token com a chave secreta
                .claim("role", role)         // Adiciona uma reivindicação com o papel do usuário
                .compact();                 // Compacta o token em uma string
        // Retorna o token encapsulado em um objeto JwtToken
        return new JwtToken(token);
    }

    // Obtém as reivindicações (claims) a partir do token JWT
    private static Claims getClaimsFromToken(String token) {
        try {
            // Analisa o token e obtém suas reivindicações
            return Jwts.parser()
                    .verifyWith(generateKey())
                    .build()
                    .parseSignedClaims((CharSequence) refactorToken(token)).getPayload();
        } catch (JwtException ex) {
            // Registra um erro se o token for inválido
            log.error(String.format("Token invalido %s", ex.getMessage()));
        }
        return null;
    }

    // Obtém o nome de usuário (e-mail) a partir das reivindicações do token
    public static String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    // Verifica se o token é válido
    public static boolean isTokenValid(String token) {
        try {
            // Tenta analisar o token e verificar sua validade
            Jwts.parser()
                    .verifyWith(generateKey())
                    .build()
                    .parseSignedClaims((CharSequence) refactorToken(token));
            return true;
        } catch (JwtException ex) {
            // Registra um erro se o token for inválido
            log.error(String.format("Token invalido %s", ex.getMessage()));
        }
        return false;
    }

    // Remove o prefixo "bearer " do token se estiver presente
    private static Object refactorToken(String token) {
        if (token.contains(JWT_BEARER)) {
            return token.substring(JWT_BEARER.length());
        }
        return token;
    }

}
