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

import com.example.userservice.dto.UserDto;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.jpa.UserRepository;
import com.example.userservice.vo.ResponseOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
public class UserServiceImpl implements UserService {

  UserRepository userRepository;
  BCryptPasswordEncoder passwordEncoder;

  @Autowired
  public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
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

    List<ResponseOrder> orders = new ArrayList<>();
    userDto.setOrders(orders);

    return userDto;
  }

  @Override
  public Iterable<UserEntity> getUserByAll() {
    return userRepository.findAll();
  }
}
