/*
 * Created by Wonuk Hwang on 2023/02/19
 * As part of Bigin
 *
 * Copyright (C) Bigin (https://bigin.io/main) - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by infra Team <wonuk_hwang@bigin.io>, 2023/02/19
 */
package com.example.userservice.client;

import com.example.userservice.vo.ResponseOrder;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
@FeignClient(name = "order-service")
public interface OrderServiceClient {

//  @GetMapping("/order-service/{userId}/orders_ng") Feign Decoder 예외처리 확인코드
@GetMapping("/order-service/{userId}/orders")
  List<ResponseOrder> getOrders(@PathVariable String userId);
}
