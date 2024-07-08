package com.learn.restapiwithboot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.restapiwithboot.config.filter.JwtAuthenticationFilter;
import com.learn.restapiwithboot.config.filter.JwtExceptionFilter;
import com.learn.restapiwithboot.config.handler.CustomAuthenticationFailureHandler;
import com.learn.restapiwithboot.config.handler.CustomAuthenticationSuccessHandler;
import com.learn.restapiwithboot.config.handler.JwtAccessDeniedHandler;
import com.learn.restapiwithboot.config.handler.JwtEntryPoint;
import com.learn.restapiwithboot.config.provider.JwtAuthenticationProvider;
import com.learn.restapiwithboot.config.provider.JwtTokenProvider;
import com.learn.restapiwithboot.config.provider.properties.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
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

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig{

    private final JwtTokenProvider jwtTokenProvider;

    private final AuthenticationConfiguration authenticationConfiguration;

    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    private final JwtEntryPoint jwtEntryPoint;

    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    private final JwtProperties jwtProperties;

    private final ObjectMapper objectMapper;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        return new JwtAuthenticationFilter(this.jwtTokenProvider, authenticationManager(), this.jwtProperties);
    }

    @Bean
    public JwtExceptionFilter jwtExceptionFilter() {
        return new JwtExceptionFilter();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/h2-console/**");
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
