package com.ky.demo_park_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ky.demo_park_api.entity.Usuario;

public interface UserRepository extends JpaRepository<Usuario, Long> {

}
