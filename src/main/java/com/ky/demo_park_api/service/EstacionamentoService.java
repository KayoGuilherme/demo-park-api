package com.ky.demo_park_api.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ky.demo_park_api.entity.Cliente;
import com.ky.demo_park_api.entity.ClienteVaga;
import com.ky.demo_park_api.entity.Vaga;
import com.ky.demo_park_api.utils.EstacionamentoUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EstacionamentoService {

    private final ClienteVagaService clienteVagaService;
    private final ClienteService clienteService;
    private final VagaService vagaService;



    @Transactional
    public ClienteVaga  checkIn(ClienteVaga clienteVaga) {
        Cliente cliente = clienteService.getClienteByCpf(clienteVaga.getCliente().getCpf());
        clienteVaga.setCliente(cliente);

        Vaga vaga = vagaService.getWaveByStatusFreedom();
        vaga.setStatus(Vaga.StatusVaga.OCUPADA);
        clienteVaga.setVaga(vaga);

        clienteVaga.setDataEntrada(LocalDateTime.now());

        clienteVaga.setRecibo(EstacionamentoUtils.gerarRecibo());

        return clienteVagaService.save(clienteVaga);


    }


}
