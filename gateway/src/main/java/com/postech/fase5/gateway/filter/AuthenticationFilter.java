package com.postech.fase5.gateway.filter;

import com.postech.fase5.gateway.exception.UnauthorizedException;
import com.postech.fase5.gateway.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator routeValidator;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = null;
            if (routeValidator.isSecured.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey("Authorization")) {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }

                String token = exchange.getRequest().getHeaders().get("Authorization").get(0);

                if (token != null && token.startsWith("Bearer ")) {
                    token = token.substring(7);
                }

                try {
                    jwtUtil.validateToken(token);

                    var role = jwtUtil.extractRole(token);

                    if (role.equals("CUSTOMER")) {
                        var cartId = jwtUtil.extractCustId(token);
                        var userId = jwtUtil.extractUserId(token);
                        request = exchange.getRequest()
                                .mutate()
                                .header("cartId", cartId)
                                .header("userId", userId.toString())
                                .build();
                    }

                    String requiredRole = routeValidator.getRequiredRole(exchange.getRequest());

                    if (requiredRole != null && !requiredRole.equals(role)) {
                        throw new UnauthorizedException(HttpStatus.UNAUTHORIZED.value(), "Unauthorized");
                    }

                } catch (Exception e) {
                    System.out.println("Error in AuthenticationFilter: " + e.getMessage());
                    throw new UnauthorizedException(HttpStatus.UNAUTHORIZED.value(), "Unauthorized");
                }
            }
            return chain.filter(exchange.mutate().request(request).build());
        });
    }

    public static class Config {

    }
}
