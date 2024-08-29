package com.ky.demo_park_api;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.ky.demo_park_api.web.dto.UserCreateDto;
import com.ky.demo_park_api.web.dto.UserResponseDto;
import com.ky.demo_park_api.web.dto.UsuarioSenhaDto;
import com.ky.demo_park_api.web.exception.ErrorMessager;

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
                .uri("api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDto("oliver2clara@gmail.com", "12345678"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UserResponseDto.class)
                .returnResult().getResponseBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getEmail()).isEqualTo("oliver2clara@gmail.com");
        assertThat(responseBody.getRole()).isEqualTo("CLIENTE");
    }

    @Test
    public void createUser_InvalidPassword_ReturnErrorMessage422() {
        ErrorMessager responseBody = testClient
                .post()
                .uri("api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDto("oliver2clara@gmail.com", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessager.class)
                .returnResult().getResponseBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
                .post()
                .uri("api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDto("oliver2clara@gmail.com", "12345"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessager.class)
                .returnResult().getResponseBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(422);

    }

    @Test
    public void createUser_InvalidEmail_ReturnErrorMessage422() {
        ErrorMessager responseBody = testClient
                .post()
                .uri("api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDto("", "12345678"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessager.class)
                .returnResult().getResponseBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
                .post()
                .uri("api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDto("kayo@", "12345678"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessager.class)
                .returnResult().getResponseBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
                .post()
                .uri("api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDto("kayo@gmail", "12345678"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessager.class)
                .returnResult().getResponseBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(422);

    }

    @Test
    public void createUser_ExistingEmail_ReturnErrorMessager409() {

        ErrorMessager responseBody = testClient
                .post()
                .uri("api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDto("kayo@gmail.com", "12345678"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorMessager.class)
                .returnResult().getResponseBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(409);
    }

    @Test
    public void updateUserPassword_PasswordIsValid_ReturnUserStatus204() {
        testClient
                .patch()
                .uri("api/v1/users/101")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDto("12345678", "123456789", "123456789"))
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    public void getUserById_WithExistingId_ReturnUserStatus200() {
        UserResponseDto responseBody = testClient
                .get()
                .uri("api/v1/users/102")
                .headers(Authentication.getHeaderAuthorization(testClient, "kayo3@gmail.com", "12345678"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponseDto.class)
                .returnResult().getResponseBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getId()).isEqualTo(102);
        assertThat(responseBody.getEmail()).isEqualTo("kayo3@gmail.com");

        responseBody = testClient
                .get()
                .uri("api/v1/users/100")
                .headers(Authentication.getHeaderAuthorization(testClient, "kayo3@gmail.com", "12345678"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponseDto.class)
                .returnResult().getResponseBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getId()).isEqualTo(100);
        assertThat(responseBody.getEmail()).isEqualTo("oliver_clara@gmail.com");

        responseBody = testClient
                .get()
                .uri("api/v1/users/100")
                .headers(Authentication.getHeaderAuthorization(testClient, "kayo3@gmail.com", "12345678"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponseDto.class)
                .returnResult().getResponseBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getId()).isEqualTo(100);
        assertThat(responseBody.getEmail()).isEqualTo("oliver_clara@gmail.com");

    }

    @Test
    public void getUserById_WithInexistingId_ReturnUserStatus404() {
        ErrorMessager responseBody = testClient
                .get()
                .uri("api/v1/users/8")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessager.class)
                .returnResult().getResponseBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(404);
    }

    @Test
    public void updateUserPassword_WithInexistingId_ReturnUserStatus404() {
        ErrorMessager responseBody = testClient
                .patch()
                .uri("api/v1/users/8")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDto("12345678", "1234567", "123456789"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessager.class)
                .returnResult().getResponseBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(422);

    }

    @Test
    public void updateUserPassword_WithInvalidFields_ReturnUserStatus422() {
        ErrorMessager responseBody = testClient
                .patch()
                .uri("api/v1/users/101")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDto("", "", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessager.class)
                .returnResult().getResponseBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
                .patch()
                .uri("api/v1/users/101")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDto("12345", "12345", "12345"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessager.class)
                .returnResult().getResponseBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void updateUserPassword_WithInvalidPasswords_ReturnUserStatus400() {
        ErrorMessager responseBody = testClient
                .patch()
                .uri("api/v1/users/101")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDto("12345678", "123456", "1234567"))
                .exchange()
                .expectStatus().isEqualTo(400)
                .expectBody(ErrorMessager.class)
                .returnResult().getResponseBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(400);

        responseBody = testClient
                .patch()
                .uri("api/v1/users/101")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDto("12345678", "123456789", "12345678"))
                .exchange()
                .expectStatus().isEqualTo(400)
                .expectBody(ErrorMessager.class)
                .returnResult().getResponseBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(400);

        responseBody = testClient
                .patch()
                .uri("api/v1/users/101")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDto("12345678", "123456789", "12345678"))
                .exchange()
                .expectStatus().isEqualTo(400)
                .expectBody(ErrorMessager.class)
                .returnResult().getResponseBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(400);
    }

    @Test
    public void getUsers_ReturnUserStatus200() {
        List<UserResponseDto> responseBody = testClient
                .get()
                .uri("api/v1/users")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserResponseDto.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.size()).isEqualTo(3);
    }
}
