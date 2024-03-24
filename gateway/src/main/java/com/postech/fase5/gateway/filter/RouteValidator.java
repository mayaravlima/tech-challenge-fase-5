package com.postech.fase5.gateway.filter;

import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public String getRequiredRole(ServerHttpRequest request) {
        String path = request.getPath().toString();

        if (request.getMethod() == HttpMethod.POST && path.startsWith("/products")) {
            return "ADMIN";
        } else if (request.getMethod() == HttpMethod.PUT && path.matches("/products/\\d+")) {
            return "ADMIN";
        } else if (request.getMethod() == HttpMethod.DELETE && path.matches("/products/\\d+")) {
            return "ADMIN";
        }

        return null;
    }

    public static final List<String> openApiEndpoints = List.of(
            "/auth/register",
            "/auth/login",
            "/eureka"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));

}
