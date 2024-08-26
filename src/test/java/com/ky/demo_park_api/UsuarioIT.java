package com.ky.demo_park_api;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.ky.demo_park_api.web.dto.UserCreateDto;
import com.ky.demo_park_api.web.dto.UserResponseDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/usuarios/usuarios-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/usuarios/usuarios-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UsuarioIT {

    @Autowired
    WebTestClient testClient;

    @Test
    public void createUser_EmailAndPasswordAreValid_ReturnUserCreatedStatus201() {
        UserResponseDto responseBody = testClient
                .post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDto("joe@gmail.com", "12345678"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UserResponseDto.class)
                .returnResult().getResponseBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getEmail()).isEqualTo("joe@gmail.com");
        assertThat(responseBody.getRole()).isEqualTo("CLIENTE");

    }
}
