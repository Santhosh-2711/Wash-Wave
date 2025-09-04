//package com.example.api_gatewayWithSecurity.filter;
//
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.gateway.filter.GatewayFilter;
//import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
//import org.springframework.http.HttpHeaders;
//import org.springframework.stereotype.Component;
//
//import com.example.api_gatewayWithSecurity.service.JwtService;
//
//@Component
//public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config>{	
//	@Autowired
//	private RouteValidator routeValidator;
//	@Autowired
//	private JwtService jwtService;
//	public AuthenticationFilter() {
//		super(Config.class);
//		
//	}
//	public static class Config{		}
//	@Override
//	public GatewayFilter apply(Config config) {
//		
//		return ((exchange,chain)->{
//			if(routeValidator.isSecured.test(exchange.getRequest()))
//			{
//				if(!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION))
//				{
//					throw new RuntimeException("missing authorization header");
//				}
//				
//				String authHeader=exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
//				if(authHeader!=null && authHeader.startsWith("Bearer ")) {
//					authHeader=authHeader.substring(7);
//				}
//				try {
//					
//					jwtService.validateToken(authHeader);
//					if(!routeValidator.ValidateUsers(authHeader,exchange.getRequest())) {
//						throw new RuntimeException("unauthorized access to application");
//					}
//					
//				}
//				catch(Exception e){
//					
//					System.out.println("unauthorized access to application");
//					throw new RuntimeException("unauthorized access to application"); 
//				}
//			}
//			
//			return chain.filter(exchange);
//		});
//	}
//	
//}

package com.example.api_gatewayWithSecurity.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import com.example.api_gatewayWithSecurity.service.JwtService;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator routeValidator;

    @Autowired
    private JwtService jwtService;

    public AuthenticationFilter() {
        super(Config.class);
    }

    public static class Config {
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (routeValidator.isSecured.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    return onError(exchange, "Missing Authorization Header", HttpStatus.UNAUTHORIZED);
                }

                String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }

                try {
                    jwtService.validateToken(authHeader);
                    if (!routeValidator.ValidateUsers(authHeader, exchange.getRequest())) {
                        return onError(exchange, "Unauthorized access to application", HttpStatus.FORBIDDEN);
                    }
                } catch (Exception e) {
                    return onError(exchange, "Invalid Token or Unauthorized", HttpStatus.UNAUTHORIZED);
                }
            }

            return chain.filter(exchange);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        response.getHeaders().add("Content-Type", "application/json");
        byte[] bytes = ("{\"error\": \"" + err + "\"}").getBytes();
        return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)));
    }
}

