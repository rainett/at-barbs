package com.rainett.atbarbsbackend.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.rainett.atbarbsbackend.dto.UserDto;
import com.rainett.atbarbsbackend.dto.auth.LoginRequest;
import com.rainett.atbarbsbackend.dto.auth.RegistrationRequest;
import com.rainett.atbarbsbackend.exception.AppException;
import com.rainett.atbarbsbackend.mapper.UserMapper;
import com.rainett.atbarbsbackend.model.users.Customer;
import com.rainett.atbarbsbackend.model.users.User;
import com.rainett.atbarbsbackend.repository.CustomerRepository;
import com.rainett.atbarbsbackend.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAuthService {
    @Value("${security.jwt.token.secret-key:secret-key}")
    private String secretKey;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public UserDto login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));
        if (matchPassword(loginRequest, user)) {
            return setTokenAndReturnDto(user);
        }
        throw new AppException("Incorrect password", HttpStatus.UNAUTHORIZED);
    }

    public UserDto register(RegistrationRequest registrationRequest) {
        if (userRepository.existsByEmail((registrationRequest.getEmail()))) {
            throw new AppException("User already exists", HttpStatus.BAD_REQUEST);
        }

        Customer customer = userMapper.registrationToEntity(registrationRequest);
        String encodedPassword = passwordEncoder.encode(registrationRequest.getPassword());
        customer.setPasswordHash(encodedPassword);
        Customer savedCustomer = customerRepository.save(customer);
        return setTokenAndReturnDto(savedCustomer);
    }

    private UserDto setTokenAndReturnDto(User savedUser) {
        UserDto userDto = userMapper.toDto(savedUser);
        userDto.setToken(createToken(userDto));
        return userDto;
    }

    private boolean matchPassword(LoginRequest loginRequest, User user) {
        return passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash());
    }

    private String createToken(UserDto userDto) {
        Date now = new Date();
        return JWT.create()
                .withIssuer(userDto.getEmail())
                .withIssuedAt(now)
                .withExpiresAt(new Date(now.getTime() + 3_600_000))
                .withClaim("userId", userDto.getId())
                .withClaim("role", userDto.getRole())
                .sign(Algorithm.HMAC256(secretKey));
    }
}
