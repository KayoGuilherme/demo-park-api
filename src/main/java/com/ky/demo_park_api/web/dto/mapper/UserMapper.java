package com.ky.demo_park_api.web.dto.mapper;


import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;

import com.ky.demo_park_api.entity.Usuario;
import com.ky.demo_park_api.web.dto.UserCreateDto;
import com.ky.demo_park_api.web.dto.UserResponseDto;


public class UserMapper {

    public static Usuario toUsuario(UserCreateDto createDto) {
        ModelMapper mapper = new ModelMapper();
        return mapper.map(createDto, Usuario.class);
    }

    public static UserResponseDto toDto(Usuario user) {
        String role = user.getRole().name().substring("ROLE_".length());
        ModelMapper mapperMain = new ModelMapper();
        TypeMap<Usuario, UserResponseDto> propertyMapper = mapperMain.createTypeMap(Usuario.class, UserResponseDto.class);
        propertyMapper.addMappings(
             mapper -> mapper.map(src -> role, UserResponseDto::setRole)
        );
        return mapperMain.map(user, UserResponseDto.class);
    }

}
