package com.main.application.repository;

import com.main.application.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Boolean existsByEmail(String email);

    Boolean existsByAccountNumber(String accountNumber);

    User findByAccountNumber(String accountNumber);
}
