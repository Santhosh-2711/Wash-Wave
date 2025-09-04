package com.example.carwash.controller;


import com.example.carwash.entity.User;

import com.example.carwash.service.JwtService;
import com.example.carwash.service.impl.UserServiceImpl;
import com.example.carwash.entity.AuthRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserServiceImpl userService;
    
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private UserServiceImpl serviceImpl;
    @Autowired
    private AuthenticationManager authenticationManager;



    @PostMapping("/signup")
    public String registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    //  Get All Users
    @GetMapping("admin/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    //  Get All Customers
    @GetMapping("admin/customers")
    public List<User> getAllCustomers() {
        return userService.getAllCustomers();
    }

    //  Get All Washers
    @GetMapping("admin/washers")
    public List<User> getAllWashers() {
        return userService.getAllWashers();
    }

    //  Update Customer Details
    @PutMapping("/customers/{id}")
    public User updateCustomer(@PathVariable Long id, @RequestBody User customerDetails) {
        return userService.updateCustomer(id, customerDetails);
    }


    // ✅ Update Washer Details
    @PutMapping("/washers/{id}")
    public User updateWasher(@PathVariable Long id, @RequestBody User washerDetails) {
    	
        return userService.updateWasher(id, washerDetails);
    }

    @GetMapping("admin/washers/{id}")
	public User getW(@PathVariable Long id){
		return userService.getWOne(id);
	}
    
    @GetMapping("admin/customers/{id}")
   	public User getC(@PathVariable Long id){
   		return userService.getCOne(id);
   	}
    
    
    
    // Delete Customer
    @DeleteMapping("/customers/{id}")
    public String deleteCustomer(@PathVariable Long id) {
        userService.deleteCustomer(id);
        return "Customer deleted successfully!";
    }

    //  Delete Washer
    @DeleteMapping("/washers/{id}")
    public String deleteWasher(@PathVariable Long id) {
        userService.deleteWasher(id);
        return "Washer deleted successfully!";
    }
    @GetMapping("admin/customers/count")
    public Integer Ccount() {
    	return userService.Ccount();
    }
    @GetMapping("admin/washers/count")
    public Integer Wcount() {
    	return userService.WCount();
    }
    
    @PostMapping("/authenticate")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );

        if (authentication.isAuthenticated()) {
            // Fetch user details from database
        	User user = serviceImpl.getUserByUsername(authRequest.getUsername());
        	System.out.println("USERNAME :"+user.getUsername());
        	
            // Generate JWT with role
            return jwtService.generateToken(authRequest.getUsername(), user.getRole().toString(), user.getId());
        } else {
            throw new UsernameNotFoundException("Invalid user request! Authentication failed.");
        }
    }
    
    @GetMapping("/users/id")
    public Long getUserIdByUsername(@RequestParam String username) {
    	return userService.getUserIdByUsername(username);
    }
    

    
//Forgot Password
  
    

@GetMapping("/fp/{username}")
    public ResponseEntity<String> getSecurityQuestion(@PathVariable String username) {
        return ResponseEntity.ok(userService.getSecurityQuestion(username));
    }




@PostMapping("/fp/verify-answer")
public ResponseEntity<String> verifyAnswer(
        @RequestParam String username,
        @RequestParam String answer) {
    
    userService.verifySecurityAnswer(username, answer);
    return ResponseEntity.ok("Answer verified successfully.");
}


@PostMapping("/fp/reset-password")
public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
    String username = request.get("username");
    String newPassword = request.get("newPassword");

    userService.resetPassword(username, newPassword);
    return ResponseEntity.ok("Password reset successfully.");
}


    
  
}
