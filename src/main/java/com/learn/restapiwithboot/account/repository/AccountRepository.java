package com.learn.restapiwithboot.account.repository;

import com.learn.restapiwithboot.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("SELECT a.id FROM Account a WHERE a.email = :email")
    Optional<Long> findIdByEmail(String email);
}
