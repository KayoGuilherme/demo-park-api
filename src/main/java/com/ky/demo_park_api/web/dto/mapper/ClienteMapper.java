package com.ky.demo_park_api.web.dto.mapper;

import org.modelmapper.ModelMapper;

import com.ky.demo_park_api.entity.Cliente;
import com.ky.demo_park_api.web.dto.ClienteCreateDto;
import com.ky.demo_park_api.web.dto.ClienteResponseDto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClienteMapper {


    public static Cliente toCLiente(ClienteCreateDto dto) {
        return new ModelMapper().map(dto, Cliente.class);
    }


    
    public static ClienteResponseDto toDto(Cliente cliente) {
        return new ModelMapper().map(cliente, ClienteResponseDto.class);
    }

}
