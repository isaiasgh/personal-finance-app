package com.isaias.finance.category_service.config.security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthInterceptor implements HandlerInterceptor {
    private final JwtProvider jwtProvider;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String header = request.getHeader("Authorization");
        if (header == null || header.isBlank()) {
            buildResponse(response, "Missing Authorization header");
            return false;
        }

        String token = header.startsWith("Bearer ") ? header.substring(7) : header;

        if (token.isBlank()) {
            buildResponse(response, "Token is empty");
            return false;
        }

        try {
            jwtProvider.validateToken(token);
            String username = jwtProvider.getUsername(token);
            request.setAttribute("username", username);
        } catch (ExpiredJwtException e) {
            buildResponse(response, "Token expired");
            return false;
        } catch (Exception e) {
            buildResponse(response, "Invalid token: " + e.getMessage());
            return false;
        }

        return true;
    }

    private void buildResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"message\": \"" + message + "\"}");
    }
}