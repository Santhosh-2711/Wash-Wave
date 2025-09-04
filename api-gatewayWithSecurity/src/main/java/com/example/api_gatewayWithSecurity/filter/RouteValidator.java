package com.example.api_gatewayWithSecurity.filter;

import java.util.List;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import com.example.api_gatewayWithSecurity.service.JwtService;
 
@Component
public class RouteValidator {
	@Autowired
	JwtService jwtservice;

	public static final List<String> openApiEndpoints=
			List.of(
					"/auth/signup",
					"/auth/authenticate",
					"/eureka",
					
					"/auth/fp",
					"/auth/fp/verify-answer",
					"/auth/fp/reset/password"
					
					
					);
					
	public static final List<String> AdminApiEndpoints=
			List.of(
					"/auth/admin/.*",
					"/auth/customers/.*",
					"/bookings/admin/.*",
					"/auth/washers/.*",
					"/payment/.*"
					);
					
	public static final List<String> WasherApiEndpoints=
			List.of(
					"/bookings/washer/.*",
					"/auth/washers/.*"
					);
					
	public static final List<String> UserApiEndpoints=
			List.of(
					"/bookings/customer/.*",
					"/auth/customers/.*",
					"/payment/.*"
					);
	
	public Predicate<ServerHttpRequest> isSecured=
			request -> openApiEndpoints.stream()
			.noneMatch(uri->request.getURI().getPath().contains(uri));

	public Predicate<ServerHttpRequest> isAdminUri=
					(request) -> AdminApiEndpoints.stream()
					.anyMatch(uri->request.getURI().getPath().matches(uri));
					
	public Predicate<ServerHttpRequest> isUserUri=
							request -> UserApiEndpoints.stream()
							.anyMatch(uri->request.getURI().getPath().matches(uri));

	public Predicate<ServerHttpRequest> isWasherUri =
							 request -> WasherApiEndpoints.stream()
							.anyMatch(uri -> request.getURI().getPath().matches(uri));
							
	public boolean ValidateUsers(String token, ServerHttpRequest request) {
		String role = jwtservice.extractRole(token);

System.out.println("Is User URI: " + isUserUri.test(request));
System.out.println("Role: " + role);
//System.out.println("Validation Result: " + ValidateUsers(token, request));


		if(isAdminUri.test(request) && role.equals("ADMIN")) {
			return true;
		}
		if(isUserUri.test(request) && role.equals("CUSTOMER")) {
			return true;
		}
		if(isWasherUri.test(request) && role.equals("WASHER")) {
			return true;
		}
		return false;
	}
}
