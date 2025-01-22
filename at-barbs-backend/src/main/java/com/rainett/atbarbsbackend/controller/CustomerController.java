package com.rainett.atbarbsbackend.controller;

import com.rainett.atbarbsbackend.dto.UserDto;
import com.rainett.atbarbsbackend.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("/{userId}")
    @PreAuthorize("isAuthenticated() and (hasRole('ROLE_ADMIN') or #userId == authentication.principal.id)")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId) {
        UserDto userDto = customerService.getCustomerById(userId);
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<String> updateUser(
            @PathVariable Long userId,
            @Valid @RequestBody UserDto userDto) {
        customerService.updateCustomer(userId, userDto);
        return ResponseEntity.ok("User profile updated successfully.");
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<UserDto>> getAllUsers(Pageable pageable) {
        Page<UserDto> users = customerService.getAllCustomers(pageable);
        return ResponseEntity.ok(users);
    }
}
