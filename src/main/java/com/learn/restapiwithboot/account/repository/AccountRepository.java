package com.learn.restapiwithboot.account.repository;

import com.learn.restapiwithboot.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {
}
