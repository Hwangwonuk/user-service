/*
 * Created by Wonuk Hwang on 2023/02/07
 * As part of Bigin
 *
 * Copyright (C) Bigin (https://bigin.io/main) - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by infra Team <wonuk_hwang@bigin.io>, 2023/02/07
 */
package com.example.userservice.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import lombok.Data;

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

@Data
// Null이 아닌것만 Json 형태로 Response 하게 만들어주는 어노테이션
@JsonInclude(Include.NON_NULL)
public class ResponseUser {
  private String email;
  private String name;
  private String userId;

  private List<ResponseOrder> orders;
}
