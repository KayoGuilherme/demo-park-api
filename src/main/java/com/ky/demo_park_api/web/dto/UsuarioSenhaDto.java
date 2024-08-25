package com.ky.demo_park_api.web.dto;

import jakarta.validation.constraints.NotBlank;
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
public class UsuarioSenhaDto {

    @Size(min = 6)
    @NotBlank(message = "O campo de senhaAtual nao pode ser nulo")
    private String senhaAtual;
    @Size(min = 6)
    @NotBlank(message = "O campo de novaSenha nao pode ser nulo")
    private String novaSenha;
    @Size(min = 6)
    @NotBlank(message = "O campo de confirmaSenha nao pode ser nulo")
    private String confirmaSenha;
}