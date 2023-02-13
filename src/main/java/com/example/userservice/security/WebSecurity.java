/*
 * Created by Wonuk Hwang on 2023/02/12
 * As part of Bigin
 *
 * Copyright (C) Bigin (https://bigin.io/main) - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by infra Team <wonuk_hwang@bigin.io>, 2023/02/12
 */
package com.example.userservice.security;


import com.example.userservice.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.IpAddressMatcher;

/**
 * create on 2023/02/12. create by IntelliJ IDEA.
 *
 * <p> </p>
 * <p> {@link } and {@link } </p> *
 *
 * @author wonukHwang
 * @version 1.0
 * @see
 * @since (ex : 5 + 5)
 */
@EnableWebSecurity
@Configuration
public class WebSecurity {

  private UserService userService;
  private BCryptPasswordEncoder bcryptPasswordEncoder;
  private Environment env;
  private AuthenticationConfiguration authenticationConfiguration;

  @Autowired
  public WebSecurity(UserService userService, BCryptPasswordEncoder bcryptPasswordEncoder,
      AuthenticationConfiguration authenticationConfiguration, Environment env) {
    this.userService = userService;
    this.bcryptPasswordEncoder = bcryptPasswordEncoder;
    this.env = env;
    this.authenticationConfiguration = authenticationConfiguration;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
//        .authorizeHttpRequests(authorize -> authorize
//            .shouldFilterAllDispatcherTypes(false)
//            .requestMatchers("/**")
//            .permitAll()
//            .anyRequest()
//            .authenticated());
        .authorizeHttpRequests((authorizeRequests) -> {
              try {
                authorizeRequests
                    .requestMatchers("/**").access(hasIpAddress("192.168.219.100"))
                    .anyRequest().authenticated()
                    .and()
                    .addFilter(getAuthenticationFilter());
              } catch (Exception e) {
                throw new RuntimeException(e);
              }
            }
        );

    http.headers().frameOptions().disable();
    return http.build();
  }

  private AuthenticationFilter getAuthenticationFilter() throws Exception {
    AuthenticationFilter authenticationFilter = new AuthenticationFilter();
    authenticationFilter.setAuthenticationManager(
        authenticationConfiguration.getAuthenticationManager()
    );

    return authenticationFilter;
  }

  private static AuthorizationManager<RequestAuthorizationContext> hasIpAddress(String ipAddress) {
    IpAddressMatcher ipAddressMatcher = new IpAddressMatcher(ipAddress);
    return (authentication, context) -> {
      HttpServletRequest request = context.getRequest();
      return new AuthorizationDecision(ipAddressMatcher.matches(request));
    };
  }

  // select pwd from users where email=?
  // db_pwd(encrypted) == input_pwd(encrypted)
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userService).passwordEncoder(bcryptPasswordEncoder);
  }
}
