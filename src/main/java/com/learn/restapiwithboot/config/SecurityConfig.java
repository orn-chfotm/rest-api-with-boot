package com.learn.restapiwithboot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.restapiwithboot.config.authentication.CustomUserDetailService;
import com.learn.restapiwithboot.config.filter.CustomAuthenticationFilter;
import com.learn.restapiwithboot.config.filter.JwtAuthenticationFilter;
import com.learn.restapiwithboot.config.filter.JwtExceptionFilter;
import com.learn.restapiwithboot.config.handler.CustomAuthenticationFailureHandler;
import com.learn.restapiwithboot.config.handler.CustomAuthenticationSuccessHandler;
import com.learn.restapiwithboot.config.handler.JwtAccessDeniedHandler;
import com.learn.restapiwithboot.config.handler.JwtEntryPoint;
import com.learn.restapiwithboot.config.provider.JwtAuthenticationProvider;
import com.learn.restapiwithboot.config.provider.JwtTokenProvider;
import com.learn.restapiwithboot.config.properties.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Collections;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig{

    private final JwtTokenProvider jwtTokenProvider;

    private final AuthenticationConfiguration authenticationConfiguration;

    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    private final JwtEntryPoint jwtEntryPoint;

    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    private final JwtProperties jwtProperties;

    private final ObjectMapper objectMapper;

    private final CustomUserDetailService customUserDetailService;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        return new JwtAuthenticationFilter(this.jwtTokenProvider, authenticationManager(null), this.jwtProperties);
    }

    @Bean
    public JwtExceptionFilter jwtExceptionFilter() {
        return new JwtExceptionFilter();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(daoAuthenticationProvider(customUserDetailService))
                .authenticationProvider(jwtAuthenticationProvider)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().mvcMatchers("/docs/index.html")
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler(jwtTokenProvider, objectMapper);
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler(objectMapper);
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public FilterRegistrationBean<CustomAuthenticationFilter> customAuthenticationFilter() throws Exception {
        CustomAuthenticationFilter filter = new CustomAuthenticationFilter(objectMapper);
        filter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
        filter.setAuthenticationFailureHandler(authenticationFailureHandler());
        filter.setAuthenticationManager(authenticationManager(null));
        filter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/login", "POST"));

        FilterRegistrationBean<CustomAuthenticationFilter> registrationBean = new FilterRegistrationBean<>(filter);
        registrationBean.setOrder(Integer.MIN_VALUE); // 최우선 순위로 설정
        registrationBean.addUrlPatterns("/api/login");
        return registrationBean;
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
            .accessDeniedHandler(jwtAccessDeniedHandler)
            .authenticationEntryPoint(jwtEntryPoint);

        http
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        http.formLogin()
                .loginProcessingUrl("/api/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .successHandler(authenticationSuccessHandler())
                .failureHandler(authenticationFailureHandler());

        return http.build();
    }

}
