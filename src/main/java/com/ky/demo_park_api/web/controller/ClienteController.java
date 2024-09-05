package com.ky.demo_park_api.web.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ky.demo_park_api.entity.Cliente;
import com.ky.demo_park_api.jwt.JwtUserDetails;
import com.ky.demo_park_api.repository.projection.ClienteProjection;
import com.ky.demo_park_api.service.ClienteService;
import com.ky.demo_park_api.service.UserService;
import com.ky.demo_park_api.web.dto.ClienteDto;
import com.ky.demo_park_api.web.dto.ClienteResponseDto;
import com.ky.demo_park_api.web.dto.PageableDto;
import com.ky.demo_park_api.web.dto.mapper.ClienteMapper;
import com.ky.demo_park_api.web.dto.mapper.PageableMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final UserService usuario;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClienteResponseDto> getById(@PathVariable Long id) {
        Cliente cliente = clienteService.getClienteById(id);
        return ResponseEntity.ok(ClienteMapper.toDto(cliente));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageableDto> getAll(@PageableDefault(size = 5, sort = {"nome_cliente"}) Pageable pageable) {
        Page<ClienteProjection> clientes = clienteService.getCliente(pageable);
        return ResponseEntity.ok(PageableMapper.toDto(clientes));
    }

    @GetMapping("/detalhes")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ClienteResponseDto> getClientDetails(@AuthenticationPrincipal JwtUserDetails userDetails) {
        Cliente cliente = clienteService.getPerUserId(userDetails.getId());
        return ResponseEntity.ok(ClienteMapper.toDto(cliente));
    }

    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ClienteResponseDto> create(@RequestBody @Valid ClienteDto data, @AuthenticationPrincipal JwtUserDetails userDetails) {
        Cliente cliente = ClienteMapper.toCLiente(data);
        cliente.setUsuario(usuario.getUserById(userDetails.getId()));
        clienteService.createClient(cliente);
        return ResponseEntity.status(201).body(ClienteMapper.toDto(cliente));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')  AND #id == authentication.principal.id ")
    public ResponseEntity<Void> updateClient(@PathVariable Long id, @Valid @RequestBody ClienteDto data) {
        Cliente cliente = clienteService.updateClient(id, data.getNome_cliente(), data.getCpf());
        return ResponseEntity.noContent().build();

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')  AND #id == authentication.principal.id ")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clienteService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
}
