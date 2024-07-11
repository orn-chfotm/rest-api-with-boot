package com.learn.restapiwithboot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.restapiwithboot.config.filter.JwtReqeustFilter;
import com.learn.restapiwithboot.config.handler.JwtAccessDeniedHandler;
import com.learn.restapiwithboot.config.handler.JwtAuthenticationEntryPoint;
import com.learn.restapiwithboot.config.properties.JwtProperties;
import com.learn.restapiwithboot.config.provider.JwtAuthenticationProvider;
import com.learn.restapiwithboot.config.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig{

    private final JwtProperties jwtProperties;

    private final JwtTokenProvider jwtTokenProvider;

    private final AuthenticationConfiguration authenticationConfiguration;

    private final ObjectMapper objectMapper;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public JwtReqeustFilter jwtReqeustFilter() throws Exception {
        return new JwtReqeustFilter(jwtTokenProvider, jwtProperties, authenticationManager());
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationProvider jwtAuthenticationProvider() {
        return new JwtAuthenticationProvider(jwtTokenProvider);
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
            .httpBasic().disable()
            .csrf().disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
            .authorizeRequests()
            .antMatchers("/api/login").permitAll() // /api/login 경로 허용
            .antMatchers("/api/auth/**").permitAll() // /api/auth 경로 허용
            .anyRequest().authenticated(); // 다른 모든 요청은 인증 필요

        http.exceptionHandling()
                .accessDeniedHandler(new JwtAccessDeniedHandler(objectMapper))
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint(objectMapper));

        http.addFilterBefore(jwtReqeustFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
