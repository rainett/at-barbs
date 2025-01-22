package com.rainett.atbarbsbackend.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.rainett.atbarbsbackend.dto.UserDto;
import com.rainett.atbarbsbackend.exception.AppException;
import com.rainett.atbarbsbackend.mapper.UserMapper;
import com.rainett.atbarbsbackend.model.users.User;
import com.rainett.atbarbsbackend.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenValidator {
    @Value("${security.jwt.token.secret-key:secret-key}")
    private String secretKey;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public Authentication validateToken(String token) {
        DecodedJWT decoded = getDecodedJWT(token);
        UserDto userDto = new UserDto();
        userDto.setEmail(decoded.getIssuer());
        userDto.setId(decoded.getClaim("userId").asLong());
        userDto.setRole(decoded.getClaim("role").asString());
        return getUsernamePasswordAuthenticationToken(userDto);
    }

    public Authentication validateTokenStrongly(String token) {
        DecodedJWT decoded = getDecodedJWT(token);
        User user = userRepository.findByEmail(decoded.getIssuer())
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));
        return getUsernamePasswordAuthenticationToken(userMapper.toDto(user));
    }

    private DecodedJWT getDecodedJWT(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

    private static UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(UserDto userDto) {
        List<GrantedAuthority> authorities = Collections.singletonList(userDto::getRole);
        return new UsernamePasswordAuthenticationToken(userDto, null, authorities);
    }
}
