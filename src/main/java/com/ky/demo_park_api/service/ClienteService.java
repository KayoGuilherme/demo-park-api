package com.ky.demo_park_api.service;


import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ky.demo_park_api.entity.Cliente;
import com.ky.demo_park_api.exception.CpfUniqueViolationException;
import com.ky.demo_park_api.repository.ClienteRepository;
import com.ky.demo_park_api.repository.projection.ClienteProjection;

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
    public Page<ClienteProjection> getCliente(Pageable pageable) {
        return clienteRepository.findAllClients(pageable);
    }

    @Transactional(readOnly = true)
    public Cliente getClienteByCpf(String cpf) {
        return clienteRepository.findByCpf(cpf).
                orElseThrow(()
                        -> new EntityNotFoundException(String.format("Usuario não encontrado.")));
    }

    @Transactional(readOnly = true)
    public Cliente getClienteById(Long id) {
        return clienteRepository.findById(id).
                orElseThrow(()
                        -> new EntityNotFoundException(String.format("Usuario não encontrado.")));
    }

    @Transactional
    public Cliente updateClient(Long id, String nome_cliente, String cpf ) {
        try {
            Cliente cliente = getClienteById(id);
            cliente.setNome_cliente(nome_cliente);
            cliente.setCpf(cpf);
            return clienteRepository.save(cliente);
        } catch (EntityNotFoundException ex) {
            throw new RuntimeException("Erro ao encontrar cliente", ex);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Erro ao atualizar cliente. Dados inválidos ou violação de integridade.", e);
        } catch (Exception e) {

            throw new RuntimeException("Erro inesperado ao atualizar cliente.", e);
        }
    }


    @Transactional
    public void deleteClient(Long id) {
        Cliente cliente = getClienteById(id);
        clienteRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Cliente getPerUserId(Long id) {
        return clienteRepository.findUserById(id);
    }

}
