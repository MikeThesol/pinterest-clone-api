package com.thesol.pinterest_clone.controller;

import com.thesol.pinterest_clone.Services.UserService;
import com.thesol.pinterest_clone.dto.JwtAuthenticationDto;
import com.thesol.pinterest_clone.dto.RefreshTokenDto;
import com.thesol.pinterest_clone.dto.UserCredentialsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    @PostMapping("/signIn")
    public ResponseEntity<JwtAuthenticationDto> signIn(@RequestBody UserCredentialsDto userCredentialsDto) {
        JwtAuthenticationDto jwtAuthenticationDto = userService.signIn(userCredentialsDto);
        return ResponseEntity.ok(jwtAuthenticationDto);
    }

    @PostMapping("/refresh")
    public JwtAuthenticationDto refresh(@RequestBody RefreshTokenDto refreshTokenDto) throws Exception {
        return userService.refreshToken(refreshTokenDto);
    }
}
