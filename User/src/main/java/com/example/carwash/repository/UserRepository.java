package com.example.carwash.repository;

import com.example.carwash.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	//Customers
	
    // Get all customers
    @Query("SELECT u FROM User u WHERE u.role = 'CUSTOMER'")
    List<User> getAllCustomers();

    // Get one customer by ID
    @Query("SELECT u FROM User u WHERE u.id = ?1 AND u.role = 'CUSTOMER'")
    User getCOne(Long userId);

    // Count of customers
    @Query("SELECT COUNT(u) FROM User u WHERE u.role = 'CUSTOMER'")
    Integer Ccount();

	//Washers
    

     // Get all washers
     @Query("SELECT u FROM User u WHERE u.role = 'WASHER'")
     List<User> getAllWashers();

     // Get one washer by ID
     @Query("SELECT u FROM User u WHERE u.id = ?1 AND u.role = 'WASHER'")
     User getWOne(Long washerId);

     // Count of washers
     @Query("SELECT COUNT(u) FROM User u WHERE u.role = 'WASHER'")
     Integer WCount();

	
	
     @Query(value="select * from users where username like :username",nativeQuery=true)
	 User findByUsername(String username);
	


}
