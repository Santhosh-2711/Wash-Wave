package com.washwave.booking.client;

import com.washwave.booking.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "UMS", url = "http://localhost:8082")
public interface UserServiceClient {
    
    @GetMapping("/users/{id}")
    UserDTO getUserById(@PathVariable Long id);
    
    @GetMapping("/users/username/{username}")
    UserDTO getUserByUsername(@PathVariable String username);
}