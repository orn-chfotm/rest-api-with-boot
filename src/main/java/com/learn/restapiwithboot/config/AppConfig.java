package com.learn.restapiwithboot.config;

import com.learn.restapiwithboot.account.domain.Account;
import com.learn.restapiwithboot.account.domain.enums.AccountRole;
import com.learn.restapiwithboot.account.repository.AccountRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class AppConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

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
                        .roles(Set.of(AccountRole.USER))
                        .build());

                accountRepository.save(Account.builder()
                        .email("admin@email.com")
                        .password(passwordEncoder.encode("1234"))
                        .roles(Set.of(AccountRole.ADMIN))
                        .build());
            }
        };
    }
}
