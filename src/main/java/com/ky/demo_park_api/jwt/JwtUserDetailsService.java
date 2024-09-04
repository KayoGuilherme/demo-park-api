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

  
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioService.getUserEmail(email);
        return new JwtUserDetails(usuario);
    }

    
    public JwtToken getTokenAuthenticated(String email) {
        Usuario.Role role = usuarioService.getRoleWhereEmail(email);
        return JwtUtils.createToken(email, role.name().substring("ROLE_".length()));
    }
}
