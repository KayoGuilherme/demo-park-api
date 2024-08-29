package com.ky.demo_park_api.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ky.demo_park_api.jwt.JwtToken;
import com.ky.demo_park_api.jwt.JwtUserDetailsService;
import com.ky.demo_park_api.web.dto.UserLoginDto;
import com.ky.demo_park_api.web.exception.ErrorMessager;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/v1")
@RestController
public class AuthController {

    private final JwtUserDetailsService detailsService;
    private final AuthenticationManager authenticationManager;


    @PostMapping("/auth")
    public ResponseEntity<?>
            authLogin(@RequestBody @Valid UserLoginDto data, HttpServletRequest request) {
        log.info("processo de autenticacao pelo login", data.getEmail());

        try {
            UsernamePasswordAuthenticationToken authenticationToken
                    = new UsernamePasswordAuthenticationToken(data.getEmail(), data.getPassword());

            authenticationManager.authenticate(authenticationToken);

            JwtToken token = detailsService.getTokenAuthenticated(data.getEmail());
            return ResponseEntity.ok(token);
        } catch (AuthenticationException ex) {
            log.warn("bad credentials from email '{}' ", data.getEmail());
        }
        return ResponseEntity.badRequest().body(new ErrorMessager(request, HttpStatus.BAD_REQUEST, "Invalid Credentials"));
    }

   

}
