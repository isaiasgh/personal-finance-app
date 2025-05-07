package com.isaias.finance.user_service.data.repository;

import com.isaias.finance.user_service.data.entity.PasswordLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPasswordLogRepository extends JpaRepository <PasswordLog, Integer> {
}