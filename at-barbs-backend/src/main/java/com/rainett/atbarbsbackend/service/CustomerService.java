package com.rainett.atbarbsbackend.service;

import com.rainett.atbarbsbackend.dto.UserDto;
import com.rainett.atbarbsbackend.exception.AppException;
import com.rainett.atbarbsbackend.mapper.UserMapper;
import com.rainett.atbarbsbackend.model.users.Customer;
import com.rainett.atbarbsbackend.repository.CustomerRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final UserMapper userMapper;

    public UserDto getCustomerById(Long userId) {
        return customerRepository.findById(userId)
                .map(userMapper::toDto)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
    }

    public void updateCustomer(Long userId, @Valid UserDto userDto) {
        Customer customer = customerRepository.findById(userId)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
        userMapper.updateEntity(customer, userDto);
        customerRepository.save(customer);
    }

    public Page<UserDto> getAllCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable).map(userMapper::toDto);
    }
}
