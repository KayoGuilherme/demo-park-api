package com.ky.demo_park_api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ky.demo_park_api.entity.ClienteVaga;
import com.ky.demo_park_api.repository.ClienteVagaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteVagaService {

    public final ClienteVagaRepository clienteVagaRepository;

    @Transactional
    public ClienteVaga save(ClienteVaga clienteVaga) {
        return clienteVagaRepository.save(clienteVaga);
    }

}
