/*
 * Created by Wonuk Hwang on 2023/02/07
 * As part of Bigin
 *
 * Copyright (C) Bigin (https://bigin.io/main) - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by infra Team <wonuk_hwang@bigin.io>, 2023/02/07
 */
package com.example.userservice.service;

import com.example.userservice.client.OrderServiceClient;
import com.example.userservice.dto.UserDto;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.jpa.UserRepository;
import com.example.userservice.vo.ResponseOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * create on 2023/02/07. create by IntelliJ IDEA.
 *
 * <p> </p>
 * <p> {@link } and {@link } </p> *
 *
 * @author wonukHwang
 * @version 1.0
 * @see
 * @since (ex : 5 + 5)
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

  UserRepository userRepository;
  BCryptPasswordEncoder passwordEncoder;

  Environment env;
  RestTemplate restTemplate;

  OrderServiceClient orderServiceClient;
  CircuitBreakerFactory circuitBreakerFactory;

  @Autowired
  public UserServiceImpl(
      UserRepository userRepository, BCryptPasswordEncoder passwordEncoder
      , Environment env, RestTemplate restTemplate, OrderServiceClient orderServiceClient,
      CircuitBreakerFactory circuitBreakerFactory) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.env = env;
    this.restTemplate = restTemplate;
    this.orderServiceClient = orderServiceClient;
    this.circuitBreakerFactory = circuitBreakerFactory
  }

  @Override
  public UserDto createUser(UserDto userDto) {
    userDto.setUserId(UUID.randomUUID().toString());

    ModelMapper mapper = new ModelMapper();
    // 완전히 일치해야만 변환하도록 설정
    mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    UserEntity userEntity = mapper.map(userDto, UserEntity.class);
    userEntity.setEncryptedPwd(passwordEncoder.encode(userDto.getPwd()));

    userRepository.save(userEntity);

    UserDto returnUserDto = mapper.map(userEntity, UserDto.class);

    return returnUserDto;
  }

  @Override
  public UserDto getUserByUserId(String userId) {
    UserEntity userEntity = userRepository.findByUserId(userId);

    if (userEntity == null) {
      throw new UsernameNotFoundException("User not found");
    }

    UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

//    List<ResponseOrder> orders = new ArrayList<>();
    /**
     * Using as Rest template
     */
//    String orderUrl = String.format(env.getProperty("order_service.url"), userId);
//    ResponseEntity<List<ResponseOrder>> orderListResponse =
//        restTemplate.exchange(orderUrl, HttpMethod.GET, null,
//            new ParameterizedTypeReference<List<ResponseOrder>>() {
//    });
//
//    List<ResponseOrder> orderList = orderListResponse.getBody();

    /**
     * Using a Feign Client
     */
    /* Feign Exception Handling */
//    List<ResponseOrder> orderList = null;
//    try {
//      orderList = orderServiceClient.getOrders(userId);
//    } catch (FeignException e) {
//      log.error(e.getMessage());
//    }
    /* Feign Error Decoder */
//    List<ResponseOrder> orderList = orderServiceClient.getOrders(userId);
//    userDto.setOrders(orderList);

    /* CircuitBreaker */
    log.info("Before call orders microservice");
    CircuitBreaker circuitbreaker = circuitBreakerFactory.create("circuitbreaker");
    List<ResponseOrder> orderList = circuitbreaker.run(() -> orderServiceClient.getOrders(userId),
        throwable -> new ArrayList<>()); // 에러가 발생하면 빈 배열을 리턴한다.
    log.info("After called orders microservice");
    userDto.setOrders(orderList);

    return userDto;
  }

  @Override
  public Iterable<UserEntity> getUserByAll() {
    return userRepository.findAll();
  }

  @Override
  public UserDto getUserDetailByEmail(String email) {
    UserEntity userEntity = userRepository.findByEmail(email);

    if (userEntity == null) {
      throw new UsernameNotFoundException(email);
    }

    UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
    return userDto;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserEntity userEntity = userRepository.findByEmail(username);

    if (userEntity == null) {
      throw new UsernameNotFoundException(username);
    }
    return new User(userEntity.getEmail(), userEntity.getEncryptedPwd(),
        true, true, true, true,
        new ArrayList<>());
  }
}
