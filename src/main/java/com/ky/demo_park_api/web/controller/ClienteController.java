package com.ky.demo_park_api.web.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ky.demo_park_api.entity.Cliente;
import com.ky.demo_park_api.jwt.JwtUserDetails;
import com.ky.demo_park_api.service.ClienteService;
import com.ky.demo_park_api.service.UserService;
import com.ky.demo_park_api.web.dto.ClienteCreateDto;
import com.ky.demo_park_api.web.dto.ClienteResponseDto;
import com.ky.demo_park_api.web.dto.mapper.ClienteMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class ClienteController {

    private final ClienteService clienteService;
    private final UserService usuario;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClienteResponseDto> getById(@PathVariable Long id) {
        Cliente cliente = clienteService.getClienteById(id);
        return ResponseEntity.ok(ClienteMapper.toDto(cliente));
    }

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Cliente>> getAll() {
        List<Cliente> clientes = clienteService.getCliente();
        return ResponseEntity.ok(clientes);
    }

    @PostMapping("cliente")
    @PreAuthorize("hasAuthority('CLIENTE')")
    public ResponseEntity<ClienteResponseDto> create(@RequestBody @Valid ClienteCreateDto data, @AuthenticationPrincipal JwtUserDetails userDetails) {
        Cliente cliente = ClienteMapper.toCLiente(data);
        cliente.setUsuario(usuario.getUserById(userDetails.getId()));
        clienteService.createClient(cliente);
        return ResponseEntity.status(201).body(ClienteMapper.toDto(cliente));
    }

}
