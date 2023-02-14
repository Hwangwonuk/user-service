/*
 * Created by Wonuk Hwang on 2023/02/13
 * As part of Bigin
 *
 * Copyright (C) Bigin (https://bigin.io/main) - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by infra Team <wonuk_hwang@bigin.io>, 2023/02/13
 */
package com.example.userservice.security;

import com.example.userservice.vo.RequestLogin;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * create on 2023/02/13. create by IntelliJ IDEA.
 *
 * <p> </p>
 * <p> {@link } and {@link } </p> *
 *
 * @author wonukHwang
 * @version 1.0
 * @see
 * @since (ex : 5 + 5)
 */
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  /**
   * 요청 정보를 처리하는 메소드
   * @param request
   * @param response
   * @return
   * @throws AuthenticationException
   */
  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request,
      HttpServletResponse response
  ) throws AuthenticationException {
    // InputStream으로 받은 이유 = POST 형식으로 데이터가 전달되기 때문. POST형식 데이터는 InputStream으로 가져와야한다.
    try {
      RequestLogin creds = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);

      return getAuthenticationManager().authenticate(
          new UsernamePasswordAuthenticationToken( // AuthenticationToken으로 바꾸어 전달
              creds.getEmail(),
              creds.getPassword(),
              new ArrayList<>())
      );
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void successfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication authResult
  ) throws IOException, ServletException {
//    super.successfulAuthentication(request, response, chain, authResult);
    logger.debug( ((User)authResult.getPrincipal()).getUsername() );
  }
}
