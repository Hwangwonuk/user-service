/*
 * Created by Wonuk Hwang on 2023/02/13
 * As part of Bigin
 *
 * Copyright (C) Bigin (https://bigin.io/main) - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by infra Team <wonuk_hwang@bigin.io>, 2023/02/13
 */
package com.example.userservice.vo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

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
@Data
public class RequestLogin {

  @NotNull(message = "Email cannot be null")
  @Size(min = 2, message = "Email not be less than two characters")
  @Email
  private String email;

  @NotNull(message = "Password cannot be null")
  @Size(min = 8, message = "Password must be equals or grater than 8 characters")
  private String password;
}
