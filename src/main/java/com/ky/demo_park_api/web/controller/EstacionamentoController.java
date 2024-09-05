package com.ky.demo_park_api.web.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ky.demo_park_api.entity.ClienteVaga;
import com.ky.demo_park_api.service.EstacionamentoService;
import com.ky.demo_park_api.web.dto.ClienteVagaDto;
import com.ky.demo_park_api.web.dto.ClienteVagaResponseDto;
import com.ky.demo_park_api.web.dto.mapper.ClienteVagaMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;





@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/estacionamento")
public class EstacionamentoController {


    private final EstacionamentoService estacionamentoService;


    @PostMapping("/check-in")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClienteVagaResponseDto> checkIn(@RequestBody @Valid ClienteVagaDto clienteVagaDto){
        ClienteVaga clienteVaga = ClienteVagaMapper.toDto(clienteVagaDto);
        estacionamentoService.checkIn(clienteVaga);
        ClienteVagaResponseDto responseDto = ClienteVagaMapper.toClienteVagaDto(clienteVaga);
          URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{recibo}")
                .buildAndExpand(clienteVaga.getRecibo())
                .toUri();
        return ResponseEntity.created(location).body(responseDto);
    }


    
}
