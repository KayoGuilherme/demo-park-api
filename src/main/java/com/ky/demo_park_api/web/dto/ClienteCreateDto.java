package com.ky.demo_park_api.web.dto;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClienteCreateDto {

    @NotNull
    @Size(min = 10, max = 125)
    private String nome_cliente;

    @NotNull
    @CPF
    @Size(min = 11, max = 11)
    private String cpf;


}
