package com.ky.demo_park_api.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ky.demo_park_api.entity.Cliente;
import com.ky.demo_park_api.repository.projection.ClienteProjection;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    boolean existsByCpf(String cpf);

    Optional<Cliente> findByCpf(String cpf);

    @Query("select c from Cliente c")
    Page<ClienteProjection> findAllClients(Pageable pageable);

    Cliente findUserById(Long id);
}
