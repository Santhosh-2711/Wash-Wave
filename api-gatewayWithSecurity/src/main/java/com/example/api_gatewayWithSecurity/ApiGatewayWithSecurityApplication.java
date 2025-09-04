package com.example.api_gatewayWithSecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayWithSecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayWithSecurityApplication.class, args);
	}

}
