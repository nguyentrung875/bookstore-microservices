package com.trungnguyen.apigateway.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.ws.rs.core.HttpHeaders;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {
    //    private final WebClient.Builder webClientBuilder;

    public AuthFilter() {
        super(Config.class);
    }

//    public AuthFilter(WebClient.Builder webClientBuilder) {
//        super(Config.class);
//        this.webClientBuilder = webClientBuilder;
//    }


    public static class Config {
        // empty class as I don't need any particular configuration
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            // Lấy JWT token từ request header
            String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new RuntimeException("Missing or invalid Authorization header");
            }

            String token = authHeader.substring(7); // Lấy token từ header

            //Verify token và lấy ra thông tin user
            Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            String username = decodedJWT.getSubject();
            if (username == "" || username == null) {
                throw new RuntimeException("Authorization error");
            }

            ServerHttpRequest request = exchange.getRequest().mutate().
                    header("X-auth-username", username).
                    build();

            return chain.filter(exchange.mutate().request(request).build());

        };
    }
}
