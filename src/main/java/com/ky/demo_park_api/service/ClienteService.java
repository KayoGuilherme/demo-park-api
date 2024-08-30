package com.ky.demo_park_api.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ky.demo_park_api.entity.Cliente;
import com.ky.demo_park_api.exception.CpfUniqueViolationException;
import com.ky.demo_park_api.repository.ClienteRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Transactional
    public Cliente createClient(Cliente cliente) {

        if (clienteRepository.existsByCpf(cliente.getCpf())) {
            throw new CpfUniqueViolationException("cpf ja cadastrado" + cliente.getCpf());
        }

        return clienteRepository.save(cliente);
    }

    @Transactional(readOnly = true)
    public List<Cliente> getCliente() {
        return clienteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Cliente getClienteByCpf(String cpf) {
        return clienteRepository.findByCpf(cpf).
                orElseThrow(()
                        -> new EntityNotFoundException(String.format("Usuario não encontrado.")));

    }

    @Transactional(readOnly = true)
    public Cliente getClienteById(Long id_cliente) {
        return clienteRepository.findById(id_cliente).
                orElseThrow(()
                        -> new EntityNotFoundException(String.format("Usuario não encontrado.")));

    }

}
