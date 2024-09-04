package com.example.gateway;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

import java.time.Duration;
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
								.addResponseHeader("X-RESPONSE-TIME", LocalDateTime.now().toString())
								.circuitBreaker(c->c.setName("accountsMicroserviceCircuitPattern").setFallbackUri("forward:/contact-support")))
						.uri("lb://ACCOUNTS"))
				.route(p -> p.path("/microservice/loans/**")
						.filters(f->f.rewritePath("/microservice/loans/(?<segment>.*)","/${segment}")
								.addResponseHeader("X-RESPONSE-TIME", LocalDateTime.now().toString())
								.retry(retryConfig -> retryConfig
										.setRetries(3)
										.setMethods(HttpMethod.GET)
										.setBackoff(Duration.ofMillis(100),Duration.ofMillis(1000),2,true)))
						.uri("lb://LOANS"))
				.route(p->p.path("/microservice/cards/**")
						.filters(f->f.rewritePath("/microservice/cards/(?<segment>.*)","/${segment}")
								.addResponseHeader("X-RESPONSE-TIME", LocalDateTime.now().toString())
								.requestRateLimiter(rateLimit ->rateLimit.setRateLimiter(getRedisRateLimiter())
										.setKeyResolver(getKeyResolver())))

						.uri("lb://CARDS")).build();
	}

	@Bean
	public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
		return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
				.circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
				.timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(6)).build()).build());
	}

	@Bean
	public RedisRateLimiter getRedisRateLimiter(){
		return new RedisRateLimiter(1,1,1);
	}

	@Bean
	public KeyResolver getKeyResolver(){
		return exchange -> Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst("user")).defaultIfEmpty("anonymous");
	}

}
