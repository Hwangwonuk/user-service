/*
 * Created by Wonuk Hwang on 2023/02/07
 * As part of Bigin
 *
 * Copyright (C) Bigin (https://bigin.io/main) - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by infra Team <wonuk_hwang@bigin.io>, 2023/02/07
 */
package com.example.userservice.controller;

import com.example.userservice.dto.UserDto;
import com.example.userservice.service.UserService;
import com.example.userservice.vo.Greeting;
import com.example.userservice.vo.RequestUser;
import com.example.userservice.vo.ResponseUser;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

@RestController
@RequestMapping("/")
public class UserController {

  private Environment env;
  private UserService userService;

  @Autowired
  private Greeting greeting;

  @Autowired
  public UserController(Environment env, UserService userService) {
    this.env = env;
    this.userService = userService;
  }

  @GetMapping("/user-service/health_check")
  public String status() {
    return String.format("It's Working in User Service on PORT %s", env.getProperty("local.server.port"));
  }

  @GetMapping("/user-service/welcome")
  public String welcome() {
//    return env.getProperty("greeting.message");
    return greeting.getMessage();
  }

  @PostMapping("/user-service/users")
  public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user) {
    ModelMapper mapper = new ModelMapper();
    mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

    UserDto userDto = mapper.map(user, UserDto.class);
    userService.createUser(userDto);

    ResponseUser responseUser = mapper.map(userDto, ResponseUser.class);

    return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
  }
}
