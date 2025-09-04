package com.example.carwash.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.example.carwash.entity.User;
import com.example.carwash.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Component
public class UserInfoUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	User userInfo = repository.findByUsername(username);

    	  if (userInfo == null) {
              throw new UsernameNotFoundException("User not found: " + username);
          }

          return new UserInfoUserDetails(userInfo);
      }
    }

