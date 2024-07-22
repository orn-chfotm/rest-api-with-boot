package com.learn.restapiwithboot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.restapiwithboot.account.repository.AccountRepository;
import com.learn.restapiwithboot.config.authentication.CustomUserDetailService;
import com.learn.restapiwithboot.config.filter.AuthenticationProcessingFilter;
import com.learn.restapiwithboot.config.filter.CustomUsernamePasswordAuthenticationFilter;
import com.learn.restapiwithboot.config.filter.JwtReqeustFilter;
import com.learn.restapiwithboot.config.handler.CustomAuthenticationFailureHandler;
import com.learn.restapiwithboot.config.handler.CustomAuthenticationSuccessHandler;
import com.learn.restapiwithboot.config.handler.JwtAccessDeniedHandler;
import com.learn.restapiwithboot.config.handler.JwtAuthenticationEntryPoint;
import com.learn.restapiwithboot.config.properties.JwtProperties;
import com.learn.restapiwithboot.config.provider.JwtAuthenticationProvider;
import com.learn.restapiwithboot.config.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.List;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig{

    private final JwtProperties jwtProperties;

    private final JwtTokenProvider jwtTokenProvider;

    private final ObjectMapper objectMapper;

    private final CustomUserDetailService customUserDetailService;

    private final CustomAuthenticationSuccessHandler authenticationSuccessHandler;

    private final CustomAuthenticationFailureHandler authenticationFailureHandler;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public JwtReqeustFilter jwtReqeustFilter() throws Exception {
        return new JwtReqeustFilter(jwtTokenProvider, jwtProperties, authenticationManager());
    }

    @Bean
    public JwtAuthenticationProvider jwtAuthenticationProvider() {
        return new JwtAuthenticationProvider(jwtTokenProvider);
    }

    @Bean
    public AuthenticationProcessingFilter authenticationProcessingFilter() throws Exception {
        AuthenticationProcessingFilter filter = new AuthenticationProcessingFilter(
                "/api/login",
                objectMapper
        );

        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(authenticationFailureHandler);

        return filter;
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        //return new ProviderManager(List.of(jwtAuthenticationProvider(), daoAuthenticationProvider()));
        authenticationManagerBuilder.authenticationProvider(jwtAuthenticationProvider());
        authenticationManagerBuilder.authenticationProvider(daoAuthenticationProvider());
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(customUserDetailService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toH2Console())
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
            .httpBasic().disable()
            .csrf().disable()
            .formLogin().disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
            .authorizeRequests()
            .antMatchers(HttpMethod.POST, "/api/account").permitAll()
            .antMatchers(
                    "/api/login",
                    "/api/auth/**",
                    "/h2-console/**"
            ).permitAll()
            .antMatchers(HttpMethod.POST, "/api/account/refresh").permitAll() // /api/auth 경로 허용
            .anyRequest().authenticated(); // 다른 모든 요청은 인증 필요

        http.exceptionHandling()
                .accessDeniedHandler(new JwtAccessDeniedHandler(objectMapper))
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint(objectMapper));

        http.addFilterBefore(jwtReqeustFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(authenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
