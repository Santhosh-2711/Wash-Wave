package com.example.carwash.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;

//
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "users")

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String email;
    private String phoneNumber;
   
	private String address;
	private int age;
    

    @Enumerated(EnumType.STRING)
    private Role role; // CUSTOMER or WASHER 


    private String securityQuestion;
    private String securityAnswerHash;


    


   
}
