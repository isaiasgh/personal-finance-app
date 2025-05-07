package com.isaias.finance.user_service.config.security;

import com.isaias.finance.user_service.data.entity.PasswordLog;
import com.isaias.finance.user_service.data.entity.User;
import com.isaias.finance.user_service.data.repository.PasswordLogRepository;
import com.isaias.finance.user_service.data.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordLogRepository passwordLogRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User with username: " + username + " not found"));
        PasswordLog passwordLog = passwordLogRepository.findPasswordLogByUserOrderByAttemptTimestampDesc(user)
                .stream()
                .filter(log -> log.getLoginError() == null && log.getHashedPassword() != null)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No valid password found for user"));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                passwordLog.getHashedPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}