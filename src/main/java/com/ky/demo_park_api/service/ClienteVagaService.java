package com.ky.demo_park_api.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ky.demo_park_api.entity.ClienteVaga;
import com.ky.demo_park_api.repository.ClienteVagaRepository;
import com.ky.demo_park_api.repository.projection.ClienteVagaProjection;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteVagaService {

    public final ClienteVagaRepository clienteVagaRepository;

    @Transactional
    public ClienteVaga save(ClienteVaga clienteVaga) {
        return clienteVagaRepository.save(clienteVaga);
    }

    @Transactional(readOnly = true)
    public ClienteVaga getRecibo(String recibo) {
        return clienteVagaRepository.findByReciboAndDataSaidaIsNull(recibo).orElseThrow(
            () ->  new EntityNotFoundException(
                String.format("Recibo '%s' não encontrado no sistema ou checkout não realizado", recibo)
            )
        );
    }

    @Transactional(readOnly = true)
    public long getTotalDeVezesEstacionamentoCompleto(String cpf) {
        return clienteVagaRepository.countByClienteCpfAndDataSaidaIsNotNull(cpf);
    }

    @Transactional(readOnly = true)
    public Page<ClienteVagaProjection> getAllByClienteCpf(String cpf, Pageable pageable) {
        return clienteVagaRepository.findAllClientesByCpf(cpf, pageable);
    }
    

}
