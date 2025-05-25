package com.isaias.finance.user_service.data.repository;

import com.isaias.finance.user_service.data.dto.UserBasicDTO;
import com.isaias.finance.user_service.data.entity.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository <User, Integer> {
    Optional <User> findByUsername (String username);

    Boolean existsUserByUsername (String username);

    Boolean existsUserByEmail (String email);
}