package com.rainett.atbarbsbackend.mapper;

import com.rainett.atbarbsbackend.dto.UserDto;
import com.rainett.atbarbsbackend.dto.auth.RegistrationRequest;
import com.rainett.atbarbsbackend.model.users.Customer;
import com.rainett.atbarbsbackend.model.users.User;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole());
        userDto.setCreatedAt(user.getCreatedAt());
        userDto.setUpdatedAt(user.getUpdatedAt());
        return userDto;
    }

    public Customer registrationToEntity(RegistrationRequest registrationRequest) {
        Customer user = new Customer();
        user.setFirstName(registrationRequest.getFirstName());
        user.setLastName(registrationRequest.getLastName());
        user.setEmail(registrationRequest.getEmail());
        return user;
    }

    public void updateEntity(User user, @Valid UserDto userDto) {
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setUpdatedAt(LocalDateTime.now());
    }
}
