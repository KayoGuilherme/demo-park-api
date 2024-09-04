package com.ky.demo_park_api.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ky.demo_park_api.entity.Vaga;
import com.ky.demo_park_api.exception.CodigoUniqueViolationException;
import com.ky.demo_park_api.repository.VagaRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VagaService {

    private final VagaRepository vagaRepository;

    @Transactional
    public Vaga createWave(Vaga vaga) {
        try {
            return vagaRepository.save(vaga);
        } catch (DataIntegrityViolationException ex) {
            throw new CodigoUniqueViolationException(
                    String.format("Vaga com codigo '%s' ja cadastrado", vaga.getCodigo())
            );
        }
    }

    @Transactional(readOnly = true)
    public List<Vaga> getWaves() {
        return vagaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Vaga getWaveByCodigo(String codigo) {
     return vagaRepository.findByCodigo(codigo)
        .orElseThrow(() -> new EntityNotFoundException("Codigo de vaga nao encontrado"));
    }


    

}
