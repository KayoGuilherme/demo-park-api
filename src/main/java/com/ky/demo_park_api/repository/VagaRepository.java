package com.ky.demo_park_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ky.demo_park_api.entity.Vaga;

public interface VagaRepository extends  JpaRepository<Vaga, Long> {

  Optional<Vaga> findByCodigo(String codigo);
    
}
