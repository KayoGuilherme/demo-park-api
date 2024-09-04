package com.ky.demo_park_api;

import java.util.function.Consumer;

import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.ky.demo_park_api.jwt.JwtToken;
import com.ky.demo_park_api.web.dto.UserLoginDto;

public class Authentication {

    public static Consumer<HttpHeaders> getHeaderAuthorization(WebTestClient client, String email, String password) {
        String token = client.post()
                .uri("/api/v1/auth").
                bodyValue(new UserLoginDto(email, password))
                .exchange()
                .expectStatus().isOk()
                .expectBody(JwtToken.class)
                .returnResult().getResponseBody().getToken();

        return headers -> headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    }
}
