package com.example.carwash.service;


import com.example.carwash.entity.User;


import java.util.List;
import java.util.Optional;

public interface UserService {
    
    String registerUser(User user);

   
    List<User> getAllUsers();
    User getUserById(Long id);
    User getUserByUsername(String username);
    
//Customer
List<User> getAllCustomers();
User updateCustomer(Long id, User customerDetails);
void deleteCustomer(Long id);
User getCOne(Long user_id);
Integer Ccount();
  
    
//Washer
List<User> getAllWashers();
User getWOne(Long washer_id);
User updateWasher(Long id, User washerDetails);
void deleteWasher(Long id);
Integer WCount();

Long getUserIdByUsername(String username);

//forgot password

String getSecurityQuestion(String username);
boolean verifySecurityAnswer(String username, String answer);
void resetPassword(String username, String newPassword);
    
}
