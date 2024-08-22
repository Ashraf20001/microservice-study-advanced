package com.example.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class SpringCloudGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringCloudGatewayApplication.class, args);
	}

	@Bean
	public RouteLocator gatewayRouteLocator(RouteLocatorBuilder routeLocatorBuilder){
		return routeLocatorBuilder.routes()
				.route(p->p.path("/microservice/accounts/**")
						.filters(f->f.rewritePath("/microservice/accounts/(?<segment>.*)","/${segment}")
								.addResponseHeader("X-RESPONSE-TIME", LocalDateTime.now().toString()))
						.uri("lb://ACCOUNTS"))
				.route(p -> p.path("/microservice/loans/**")
						.filters(f->f.rewritePath("/microservice/loans/(?<segment>.*)","/${segment}")
								.addResponseHeader("X-RESPONSE-TIME", LocalDateTime.now().toString()))
						.uri("lb://LOANS"))
				.route(p->p.path("/microservice/cards/**")
						.filters(f->f.rewritePath("/microservice/cards/(?<segment>.*)","/${segment}")
								.addResponseHeader("X-RESPONSE-TIME", LocalDateTime.now().toString()))
						.uri("lb://CARDS")).build();
	}

}
