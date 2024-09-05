package com.ky.demo_park_api.web.dto.mapper;


import org.modelmapper.ModelMapper;

import com.ky.demo_park_api.entity.Vaga;
import com.ky.demo_park_api.web.dto.VagaDto;
import com.ky.demo_park_api.web.dto.VagaResponseDto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VagaMapper {

    public static Vaga toVaga(VagaDto dto) {
        return new ModelMapper().map(dto, Vaga.class);
    }

    public static VagaResponseDto toDto(Vaga vaga) {
        return new ModelMapper().map(vaga, VagaResponseDto.class);
    }
}
