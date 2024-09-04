package com.ky.demo_park_api.service;

import java.util.List;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ky.demo_park_api.entity.Usuario;
import com.ky.demo_park_api.entity.Usuario.Role;
import com.ky.demo_park_api.exception.EmailUniqueViolationException;
import com.ky.demo_park_api.exception.EntityNotFoundException;
import com.ky.demo_park_api.exception.PasswordInvalidException;
import com.ky.demo_park_api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Usuario salvar(Usuario usuario) {

        if (userRepository.existsByEmail(usuario.getEmail())) {
            throw new EmailUniqueViolationException("Email já cadastrado: " + usuario.getEmail());
        }

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        return userRepository.save(usuario);

    }

    @Transactional(readOnly = true)
    public Usuario getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(()
                        -> new EntityNotFoundException(String.format("Usuario não encontrado.")));
    }

    @Transactional(readOnly = true)
    public Usuario getUserEmail(String email) {
        return userRepository.findUserByEmail(email).
                orElseThrow(()
                        -> new EntityNotFoundException(String.format("Usuario não encontrado.")));
    }

    @Transactional
    public Usuario updatePassword(Long id, String senhaAtual, String confirmaSenha, String novaSenha) {

        Usuario user = getUserById(id);

        if (!user.getPassword().equals(senhaAtual)) {
            throw new PasswordInvalidException(String.format("Sua senha não confere."));
        }

        if (!passwordEncoder.matches(senhaAtual, user.getPassword() )) {
            throw new PasswordInvalidException(String.format("Nova senha não confere com o confirmar senha"));
        }

        user.setPassword(passwordEncoder.encode(novaSenha));
        return user;
    }

    @Transactional(readOnly = true)
    public List<Usuario> getUser() {
        return userRepository.findAll();
    }

    @Transactional
    public Usuario deleteUser(Long id) {
        Usuario user = getUserById(id);
        userRepository.deleteById(id);
        return user;
    }

    public Role getRoleWhereEmail(String email) {
        return userRepository.findRoleByEmail(email);
    }

}
