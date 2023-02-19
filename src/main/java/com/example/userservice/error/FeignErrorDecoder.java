/*
 * Created by Wonuk Hwang on 2023/02/19
 * As part of Bigin
 *
 * Copyright (C) Bigin (https://bigin.io/main) - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by infra Team <wonuk_hwang@bigin.io>, 2023/02/19
 */
package com.example.userservice.error;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

/**
 * create on 2023/02/19. create by IntelliJ IDEA.
 *
 * <p> </p>
 * <p> {@link } and {@link } </p> *
 *
 * @author wonukHwang
 * @version 1.0
 * @see
 * @since (ex : 5 + 5)
 */
@Component
public class FeignErrorDecoder implements ErrorDecoder {

  Environment env;

  @Autowired
  public FeignErrorDecoder(Environment env) {
    this.env = env;
  }

  @Override
  public Exception decode(String methodKey, Response response) {
    switch (response.status()) {
      case 400:
        break;
      case 404:
        if (methodKey.contains("getOrders")) {
          return new ResponseStatusException(HttpStatus.valueOf(response.status()),
              env.getProperty("order_service.exception.order_is_empty"));
        }
        break;
      default:
        return new Exception(response.reason());
    }
    return null;
  }
}
