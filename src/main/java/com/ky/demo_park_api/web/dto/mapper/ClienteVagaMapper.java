package com.ky.demo_park_api.web.dto.mapper;

import org.modelmapper.ModelMapper;

import com.ky.demo_park_api.entity.ClienteVaga;
import com.ky.demo_park_api.web.dto.ClienteVagaDto;
import com.ky.demo_park_api.web.dto.ClienteVagaResponseDto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClienteVagaMapper {
    

    public static ClienteVaga toDto(ClienteVagaDto vagaDto){
        return new ModelMapper().map(vagaDto, ClienteVaga.class);
    }

    public static ClienteVagaResponseDto toClienteVagaDto(ClienteVaga clienteVaga) {
        return new ModelMapper().map(clienteVaga, ClienteVagaResponseDto.class);
    }



}
