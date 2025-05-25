package com.isaias.finance.category_service.config.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FeignClientInterceptor implements RequestInterceptor {
    private final HttpServletRequest request;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null) {
            requestTemplate.header("Authorization", authorizationHeader);
        }
    }
}