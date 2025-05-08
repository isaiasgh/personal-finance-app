package com.isaias.finance.user_service.config.security;

import com.isaias.finance.user_service.data.entity.User;
import com.isaias.finance.user_service.data.repository.UserRepository;
import com.isaias.finance.user_service.domain.service.AuthLogService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtProvider jwtProvider;
    private final AuthLogService authLogService;
    private final UserRepository userRepository;

    public String getRequestToken (HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken)) {
            if (bearerToken.startsWith("Bearer ")) return bearerToken.substring(7);
            else return bearerToken;
        }

        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getRequestToken(request);

        if (StringUtils.hasText(token)) {
            try {
                jwtProvider.validateToken(token);
                String username = jwtProvider.getUsernameFromJWT(token);
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } catch (ExpiredJwtException ex) {
                String username = ex.getClaims().getSubject();
                User user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found."));
                authLogService.logAuthError(user, "Token expired", LocalDateTime.now());
            }
        }

        filterChain.doFilter(request, response);
    }
}
