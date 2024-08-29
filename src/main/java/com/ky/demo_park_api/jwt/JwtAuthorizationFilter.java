package com.ky.demo_park_api.jwt;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

// Anotação Lombok para criar um logger estático para a classe
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    // Injeção de dependência do serviço que carrega detalhes do usuário
    @Autowired
    private JwtUserDetailsService detailsService;

    // Método principal do filtro, chamado para cada requisição
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Obtém o cabeçalho "Authorization" da requisição
        final String header = request.getHeader(JwtUtils.JWT_AUTHORIZATION);

        // Verifica se o cabeçalho está presente e começa com "Bearer "
        if (header == null || !header.startsWith(JwtUtils.JWT_BEARER)) {
            // Se não houver token ou não começar com "Bearer ", loga a mensagem e continua o filtro
            log.info("JWT Token está nulo, vazio ou não iniciado com 'Bearer '.");
            filterChain.doFilter(request, response);
            return;
        }

        // Remove o prefixo "Bearer " do token
        final String token = header.substring(JwtUtils.JWT_BEARER.length()).trim(); 

        // Verifica se o token é válido
        if (!JwtUtils.isTokenValid(token)) {
            // Se o token for inválido ou expirado, loga a mensagem e continua o filtro
            log.warn("JWT Token está inválido ou expirado.");
            filterChain.doFilter(request, response);
            return;
        }

        // Obtém o e-mail do token JWT
        String email = JwtUtils.getEmailFromToken(token);
        // Configura a autenticação com base no e-mail extraído do token
        toAuthentication(request, email);

        // Continua o filtro para a próxima etapa
        filterChain.doFilter(request, response);
    }

    // Método para configurar a autenticação do usuário
    private void toAuthentication(HttpServletRequest request, String email) {
        // Carrega os detalhes do usuário usando o e-mail
        UserDetails userDetails = detailsService.loadUserByUsername(email);

        // Cria um token de autenticação com os detalhes do usuário e suas autoridades
        UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken
                .authenticated(userDetails, null, userDetails.getAuthorities());

        // Adiciona os detalhes da requisição ao token de autenticação
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // Define o token de autenticação no contexto de segurança
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
