package com.ky.demo_park_api.jwt;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ky.demo_park_api.entity.Usuario;
import com.ky.demo_park_api.service.UserService;

// Implementa a interface UserDetailsService para fornecer detalhes do usuário para autenticação
@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService usuarioService;

    public JwtUserDetailsService(UserService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Implementação do método da interface UserDetailsService para carregar detalhes do usuário com base no e-mail
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Obtém o usuário com base no e-mail
        Usuario usuario = usuarioService.getUserEmail(email);
        // Retorna uma instância de JwtUserDetails com as informações do usuário
        return new JwtUserDetails(usuario);
    }

    // Método para obter um token JWT autenticado para um e-mail específico
    public JwtToken getTokenAuthenticated(String email) {
        Usuario.Role role = usuarioService.getRoleWhereEmail(email);
        // Cria um token JWT usando o e-mail e o papel do usuário
        return JwtUtils.createToken(email, role.name().substring("ROLE_".length()));
    }
}
