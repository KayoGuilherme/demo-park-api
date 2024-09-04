package com.ky.demo_park_api.web.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VagaDto {

    @NotNull
    @Size(min = 4, max = 4)
    private String codigo;

    @NotNull
    private String status;

}
