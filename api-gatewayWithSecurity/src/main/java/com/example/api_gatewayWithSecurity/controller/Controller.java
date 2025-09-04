package com.example.api_gatewayWithSecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.server.reactive.ServerHttpRequest;
import com.example.api_gatewayWithSecurity.filter.RouteValidator;
import com.example.api_gatewayWithSecurity.service.JwtService;

@RestController
@RequestMapping("/API")
public class Controller {
	@Autowired
	JwtService service;
	@GetMapping("/extract/{token}")
	public String extractToken(@PathVariable String token) {
	    return service.extractRole(token);
	}
	@Autowired
	private RouteValidator routeValidator;

	@GetMapping("/validate-route")
	public ResponseEntity<String> checkRoute(ServerHttpRequest request) {
	    boolean isSecured = routeValidator.isSecured.test(request);

	    if (isSecured) {
	        return ResponseEntity.ok("The route '" + request.getURI().getPath() + "' is **SECURED** (Requires Authentication)");
	    } else {
	        return ResponseEntity.ok("The route '" + request.getURI().getPath() + "' is **OPEN** (No Authentication Needed)");
	    }
	}
	
	
	

}


   

