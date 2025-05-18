package com.isaias.finance.category_service.domain.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("user-service")
public interface UserAuthClient {
    @GetMapping("/user-service/users/{username}")
    boolean isUsernameValid (@PathVariable String username, @RequestHeader ("Authorization") String jwtAuth);
}