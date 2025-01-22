package com.rainett.atbarbsbackend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtTokenValidator jwtTokenValidator;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException,
            IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null) {
            String[] authElements =
                    header.split(" "); // contains two parts: Bearer key-word, and token itself
            if (authElements.length == 2 && "Bearer".equals(authElements[0])) {
                try {
                    authenticate(request.getMethod(), authElements[1]);
                } catch (RuntimeException ex) {
                    createAuthenticationErrorResponse(response, ex);
                    return;
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    private static void createAuthenticationErrorResponse(HttpServletResponse response,
                                                          RuntimeException ex) throws IOException {
        SecurityContextHolder.clearContext();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());

        String jsonError = new ObjectMapper().writeValueAsString(errorResponse);
        response.getWriter().write(jsonError);
    }

    private void authenticate(String method, String token) {
        Authentication authentication;
        if ("GET".equals(method)) {
            authentication = jwtTokenValidator.validateToken(token);
        } else {
            authentication = jwtTokenValidator.validateTokenStrongly(token);
        }

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);
    }
}
