package com.isaias.finance.user_service.data.repository;

import com.isaias.finance.user_service.data.entity.PasswordLog;
import com.isaias.finance.user_service.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PasswordLogRepository extends JpaRepository <PasswordLog, Integer> {
    List<PasswordLog> findPasswordLogByUserAndIsCurrent(User user, Boolean isCurrent);

    List<PasswordLog> findPasswordLogByUser (User user);
}