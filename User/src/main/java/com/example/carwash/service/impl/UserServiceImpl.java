package com.example.carwash.service.impl;



import com.example.carwash.entity.User;
import com.example.carwash.exception.InvalidUserException;
import com.example.carwash.exception.UserNotFoundException;
import com.example.carwash.repository.UserRepository;

import com.example.carwash.service.UserService;


import jakarta.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
public  class UserServiceImpl implements UserService {

    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private UserRepository userRepository;
    

    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    
    private void validateUser(User user) {
        StringBuilder errors = new StringBuilder();

        if (user.getUsername() == null || !user.getUsername().matches("^[A-Za-z]+$")) {
            errors.append("Username must contain only alphabetical characters. ");
        }
        if (user.getPassword() == null || !user.getPassword().matches("^\\d{4}$")) {
            errors.append("Password must be exactly 4 digits. ");
        }
        if (user.getEmail() == null || !user.getEmail().matches("^[A-Za-z0-9._%+-]+@gmail\\.com$")) {
            errors.append("Email must be a valid Gmail address. ");
        }
        if (user.getPhoneNumber() == null || !user.getPhoneNumber().matches("^\\d{10}$")) {
            errors.append("Phone number must be exactly 10 digits. ");
        }
        if (user.getAge() <= 18) {
            errors.append("Age must be greater than 18. ");
        }

        if (errors.length() > 0) {
            throw new InvalidUserException(errors.toString().trim());
        }
    }
    
    
    
    
    
    @Override
    public String registerUser(User user) {
        if (user.getRole() == null) {
            throw new RuntimeException("Role is required for user registration.");
        }
        validateUser(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "User registered successfully!";
    }

    
    @Override
    public List<User> getAllUsers() {
//        return userRepository.findAll();
    	logger.info("Fetching all Users");
		try {
			List<User> userList = userRepository.findAll();
			logger.info("Fetched {} Users", userList.size());
			return userList;
		} catch (Exception e) {
			logger.error("Error occurred while fetching Users: {}", e.getMessage(), e);
			return null;
		}
    }

    @Override
    public User getUserById(Long id) {

    	logger.info("Fetching User with id: {}", id);
		try {
			User user = userRepository.findById(id).orElseThrow();
			logger.info("Fetched User: {}", user);
			return user;
		} catch (Exception e) {
			logger.error("Error occurred while fetching User with id {}: {}", id, e.getMessage(), e);
			return null;
		}
    }

    
    @Override
    public List<User> getAllCustomers() {

    	logger.info("Fetching all Customers");
		try {
			List<User> customerList = userRepository.getAllCustomers();
			logger.info("Fetched {} Customers", customerList.size());
			return customerList;
		} catch (Exception e) {
			logger.error("Error occurred while fetching Customers: {}", e.getMessage(), e);
			return null;
		}
		
    }


    
    @Override
	public User getUserByUsername(String username) {
    	try {
	    return userRepository.findByUsername(username);
	    }
	catch(Exception e) {
	            throw new EntityNotFoundException("User not found: " + username);}
	}


    @Override
    public User updateCustomer(Long id, User customerDetails) {
        validateUser(customerDetails);
        User existingCustomer = getCOne(id);
        existingCustomer.setUsername(customerDetails.getUsername());
        String encodedPassword = passwordEncoder.encode(customerDetails.getPassword());
        existingCustomer.setPassword(encodedPassword);
        existingCustomer.setEmail(customerDetails.getEmail());
        existingCustomer.setPhoneNumber(customerDetails.getPhoneNumber());
        existingCustomer.setAddress(customerDetails.getAddress());
        existingCustomer.setAge(customerDetails.getAge());
        return userRepository.save(existingCustomer);
    }

    @Override
    public void deleteCustomer(Long id) {

    	User customer=getCOne(id);
    	logger.info("Request received to delete customer: {}", customer);
		try {
			userRepository.delete(customer);
			logger.info("User deleted successfully: {}", customer);
		} catch (Exception e) {
			logger.error("Error occurred while deleting Customer: {}", e.getMessage(), e);
		}
    }

    
    @Override
    public List<User> getAllWashers() {

    	logger.info("Fetching all Washers");
		try {
			List<User> washerList = userRepository.getAllWashers();
			logger.info("Fetched {} Users", washerList.size());
			return washerList;
		} catch (Exception e) {
			logger.error("Error occurred while fetching Washers: {}", e.getMessage(), e);
			return null;
		}
    }


 

    @Override
    public User updateWasher(Long id, User washerDetails) {
        validateUser(washerDetails);
        User existingWasher = getWOne(id);
        existingWasher.setUsername(washerDetails.getUsername());
        String encodedPassword = passwordEncoder.encode(washerDetails.getPassword());
        existingWasher.setPassword(encodedPassword);
        existingWasher.setPhoneNumber(washerDetails.getPhoneNumber());
        existingWasher.setAddress(washerDetails.getAddress());
        existingWasher.setAge(washerDetails.getAge());
        return userRepository.save(existingWasher);
    }

    @Override
    public void deleteWasher(Long id) {

    	User washer=getWOne(id);
    	logger.info("Request received to delete washer: {}", washer);
		try {
			userRepository.delete(washer);
			logger.info("User deleted successfully: {}", washer);
		} catch (Exception e) {
			logger.error("Error occurred while deleting Washer: {}", e.getMessage(), e);
		}
    }


	
	@Override
	public User getWOne(Long washer_id) {
		logger.info("Fetching Washer with id: {}", washer_id);
		try {
			User washer = userRepository.getWOne(washer_id);
			logger.info("Fetched Washer: {}", washer);
			return washer;
		} catch (Exception e) {
			logger.error("Error occurred while fetching Washer with id {}: {}", washer_id, e.getMessage(), e);
			return null;
		}
	}


	@Override
	public User getCOne(Long id) {

		logger.info("Fetching User with id: {}", id);
		User customer = userRepository.getCOne(id);
		if (customer == null) {
        logger.warn("No customer found with id: {}", id);
        throw new UserNotFoundException("No customer with id: " + id);
    }
    logger.info("Fetched User: {}", customer);
    return customer;
}



	@Override
	public Integer Ccount() {
		// TODO Auto-generated method stub
		
		return userRepository.Ccount();
	}


	@Override
	public Integer WCount() {
		// TODO Auto-generated method stub
		return userRepository.WCount();
	}





	@Override
	public Long getUserIdByUsername(String username) {
		 User user = userRepository.findByUsername(username);
		 return user.getId();
	}
	
	

//Forgot password



public String getSecurityQuestion(String username) {
    User user = userRepository.findByUsername(username);
    if (user == null) {
        throw new UserNotFoundException("User with username '" + username + "' not found.");
    }
    return user.getSecurityQuestion();
}



public boolean verifySecurityAnswer(String username, String answer) {
    User user = userRepository.findByUsername(username);
    if (user == null) {
        throw new UserNotFoundException("User with username '" + username + "' not found.");
    }

    if (!answer.equals(user.getSecurityAnswerHash())) {
        throw new InvalidUserException("Security answer is incorrect.");
    }

    return true;
}



public void resetPassword(String username, String newPassword) {
    User user = userRepository.findByUsername(username);
    user.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(user);
}



	




	
}
