package com.ky.demo_park_api.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ky.demo_park_api.jwt.JwtToken;
import com.ky.demo_park_api.jwt.JwtUserDetailsService;
import com.ky.demo_park_api.web.dto.UserLoginDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("auth")
@RestController
public class AuthController {

    private final JwtUserDetailsService detailsService;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<JwtToken>
     authLogin(@RequestBody @Valid UserLoginDto data, HttpServletRequest request){ 
        log.info("processo de autenticacao pelo login", data.getEmail());

        try {       
            
        } catch (Exception e) {
        
        }
    }

}
