package com.ky.demo_park_api.web.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ky.demo_park_api.entity.Vaga;
import com.ky.demo_park_api.service.VagaService;
import com.ky.demo_park_api.web.dto.VagaDto;
import com.ky.demo_park_api.web.dto.VagaResponseDto;
import com.ky.demo_park_api.web.dto.mapper.VagaMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/vagas")
public class VagaController {

    private final VagaService vagaService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> create(@RequestBody @Valid VagaDto dto) {
        Vaga vaga = VagaMapper.toDto(dto);
        vagaService.createWave(vaga);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{codigo}")
                .buildAndExpand(vaga.getCodigo())
                .toUri();
        return ResponseEntity.created(location).build();
    }
    
    @GetMapping("/{codigo}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VagaResponseDto> getWaveByCode(@PathVariable String codigo) {
        Vaga vaga = vagaService.getWaveByCodigo(codigo);
        return ResponseEntity.ok(VagaMapper.toVagaDto(vaga));
    }

}
