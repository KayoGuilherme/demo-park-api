package com.ky.demo_park_api.web.dto;

import org.hibernate.validator.constraints.br.CPF;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClienteVagaDto {

   @NotNull
    @Size(min = 8, max = 8)
    @Pattern(regexp = "[A-Z]{3}-[0-9]{4}", message = "A placa do veículo deve seguir o padrão 'XXX-0000'")
    private String placa;
    @NotNull
    private String marca;
    @NotNull
    private String modelo;
    @NotNull
    private String cor;
    @NotNull
    @Size(min = 11, max = 11)
    @CPF
    private String clienteCpf;

}
