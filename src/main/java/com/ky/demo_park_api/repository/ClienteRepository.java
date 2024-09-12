package com.ky.demo_park_api.repository;



import com.ky.demo_park_api.entity.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import com.ky.demo_park_api.repository.projection.ClienteProjection;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    boolean existsByCpf(String cpf);
    boolean existsByUsuarioId(Long id);

    Optional<Cliente> findByCpf(String cpf);


    @Query("select c from Cliente c")
    Page<ClienteProjection> findAllClients(Pageable pageable);

    Cliente findByUsuarioId(Long id);
}

