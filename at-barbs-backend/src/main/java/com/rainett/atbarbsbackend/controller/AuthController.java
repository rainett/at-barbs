package com.rainett.atbarbsbackend.controller;

import com.rainett.atbarbsbackend.dto.auth.LoginRequest;
import com.rainett.atbarbsbackend.dto.auth.RegistrationRequest;
import com.rainett.atbarbsbackend.dto.UserDto;
import com.rainett.atbarbsbackend.service.UserAuthService;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final UserAuthService userAuthService;

    @PostMapping("/login")
    @PreAuthorize("permitAll()")
    public ResponseEntity<UserDto> login(@Valid @RequestBody LoginRequest loginRequest) {
        UserDto userDto = userAuthService.login(loginRequest);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/register")
    @PreAuthorize("permitAll()")
    public ResponseEntity<UserDto> register(@Valid @RequestBody RegistrationRequest registrationRequest) {
        UserDto userDto = userAuthService.register(registrationRequest);
        return ResponseEntity.created(URI.create("/users/" + userDto.getId())).body(userDto);
    }

}