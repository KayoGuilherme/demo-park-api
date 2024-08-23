package com.ky.demo_park_api.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ky.demo_park_api.entity.Usuario;
import com.ky.demo_park_api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public Usuario salvar(Usuario usuario) {
        return userRepository.save(usuario);
    }

    @Transactional(readOnly = true)
    public Usuario getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User not found!")
        );
    }

    @Transactional
    public Usuario updatePassword(String password, Long id) {
        Usuario user = getUserById(id);
        user.setPassword(password);
        return user;
    }

    @Transactional(readOnly = true)
    public List<Usuario> getUser() {
        return userRepository.findAll();
    }

}
