package com.ky.demo_park_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ky.demo_park_api.entity.Usuario;

public interface UserRepository extends JpaRepository<Usuario, Long> {

    boolean existsByEmail(String email);

    Optional<Usuario> findUserByEmail(String email);

    @Query("select u.role from Usuario u where u.email like :email")
    public Usuario.Role findRoleByEmail(@Param("email") String email);
}
