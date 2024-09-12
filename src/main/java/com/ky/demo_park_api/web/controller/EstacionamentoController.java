package com.ky.demo_park_api.web.controller;

import com.ky.demo_park_api.entity.ClienteVaga;
import com.ky.demo_park_api.jwt.JwtUserDetails;
import com.ky.demo_park_api.repository.projection.ClienteVagaProjection;
import com.ky.demo_park_api.service.ClienteService;
import com.ky.demo_park_api.service.ClienteVagaService;
import com.ky.demo_park_api.service.EstacionamentoService;
import com.ky.demo_park_api.service.JasperService;
import com.ky.demo_park_api.web.dto.ClienteVagaDto;
import com.ky.demo_park_api.web.dto.ClienteVagaResponseDto;
import com.ky.demo_park_api.web.dto.PageableDto;
import com.ky.demo_park_api.web.dto.mapper.ClienteVagaMapper;
import com.ky.demo_park_api.web.dto.mapper.PageableMapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/estacionamento")
public class EstacionamentoController {

    private final EstacionamentoService estacionamentoService;
    private final ClienteVagaService clienteVagaService;
    private final ClienteService clienteService;
    private final JasperService jasperService;

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
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
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

    @GetMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<PageableDto> getAllEstacionamentosDoCliente(@AuthenticationPrincipal JwtUserDetails user,
                                                                      @PageableDefault(size = 5, sort = "dataEntrada", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<ClienteVagaProjection> projection = clienteVagaService.getAllByClienteId(user.getId(), pageable);

        PageableDto dto = PageableMapper.toDto(projection);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/relatorio")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<Void> getRelatorioPerCpf(HttpServletResponse response, @AuthenticationPrincipal JwtUserDetails user) throws IOException {
        String cpf = clienteService.getPerUserId(user.getId()).getCpf();
        jasperService.addParams("CPF", cpf);

        byte[] bytes = jasperService.gerarPdf();

        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setHeader("Content-Disposition", "inline; filename=" + System.currentTimeMillis() + ".pdf");
        response.getOutputStream().write(bytes);

        return ResponseEntity.ok().build();
    }


}
