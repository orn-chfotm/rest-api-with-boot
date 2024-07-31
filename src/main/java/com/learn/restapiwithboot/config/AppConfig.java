package com.learn.restapiwithboot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.learn.restapiwithboot.account.domain.Account;
import com.learn.restapiwithboot.account.domain.enums.AccountRole;
import com.learn.restapiwithboot.account.domain.enums.Gender;
import com.learn.restapiwithboot.account.repository.AccountRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

@Configuration
public class AppConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return objectMapper;
    }

    /* Test Data */
    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {

            @Autowired
            AccountRepository accountRepository;

            @Autowired
            PasswordEncoder passwordEncoder;

            @Override
            public void run(ApplicationArguments args) throws Exception {
                accountRepository.save(Account.builder()
                        .email("user@email.com")
                        .password(passwordEncoder.encode("1234"))
                        .role(AccountRole.USER)
                        .gender(Gender.getName("M"))
                        .phoneNumber("010-1234-5678")
                        .build());

                accountRepository.save(Account.builder()
                        .email("admin@email.com")
                        .password(passwordEncoder.encode("1234"))
                        .role(AccountRole.ADMIN)
                        .gender(Gender.getName("F"))
                        .phoneNumber("010-1234-5678")
                        .build());
            }
        };
    }
}
