package com.ky.demo_park_api.web.controller;

import java.net.URI;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ky.demo_park_api.entity.ClienteVaga;
import com.ky.demo_park_api.repository.projection.ClienteVagaProjection;
import com.ky.demo_park_api.service.ClienteVagaService;
import com.ky.demo_park_api.service.EstacionamentoService;
import com.ky.demo_park_api.web.dto.ClienteVagaDto;
import com.ky.demo_park_api.web.dto.ClienteVagaResponseDto;
import com.ky.demo_park_api.web.dto.PageableDto;
import com.ky.demo_park_api.web.dto.mapper.ClienteVagaMapper;
import com.ky.demo_park_api.web.dto.mapper.PageableMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/estacionamento")
public class EstacionamentoController {

    private final EstacionamentoService estacionamentoService;
    private final ClienteVagaService clienteVagaService;

    @PostMapping("/check-in")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClienteVagaResponseDto> checkIn(@RequestBody @Valid ClienteVagaDto clienteVagaDto) {
        ClienteVaga clienteVaga = ClienteVagaMapper.toDto(clienteVagaDto);
        estacionamentoService.checkIn(clienteVaga);
        ClienteVagaResponseDto responseDto = ClienteVagaMapper.toClienteVagaDto(clienteVaga);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{recibo}")
                .buildAndExpand(clienteVaga.getRecibo())
                .toUri();

        return ResponseEntity.created(location).body(responseDto);
    }

    @GetMapping("/check-in/{recibo}")
    @PreAuthorize("hasAnyRole('ADMIN'), 'CLIENTE")
    public ResponseEntity<ClienteVagaResponseDto> getByRecibo(@PathVariable String recibo) {
        ClienteVaga clienteVaga = clienteVagaService.getRecibo(recibo);
        ClienteVagaResponseDto response = ClienteVagaMapper.toClienteVagaDto(clienteVaga);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/check-out/{recibo}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClienteVagaResponseDto> checkout(@PathVariable String recibo) {
        ClienteVaga clienteVaga = estacionamentoService.checkout(recibo);
        ClienteVagaResponseDto response = ClienteVagaMapper.toClienteVagaDto(clienteVaga);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cpf/{cpf}")
    @PreAuthorize("hasRole('ADMIN')")   
    public ResponseEntity<PageableDto> getAllEstacionamentosByCpf(@PathVariable String cpf,
            @PageableDefault(size = 5, sort = "dataEntrada", direction = Sort.Direction.ASC) Pageable pageable) {
                
        Page<ClienteVagaProjection> projection = clienteVagaService.getAllByClienteCpf(cpf, pageable);

        PageableDto dto = PageableMapper.toDto(projection);
        return ResponseEntity.ok(dto);

    }
}
