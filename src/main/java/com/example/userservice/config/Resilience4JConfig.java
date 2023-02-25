/*
 * Created by Wonuk Hwang on 2023/02/25
 * As part of Bigin
 *
 * Copyright (C) Bigin (https://bigin.io/main) - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by infra Team <wonuk_hwang@bigin.io>, 2023/02/25
 */
package com.example.userservice.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import java.time.Duration;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * create on 2023/02/25. create by IntelliJ IDEA.
 *
 * <p> </p>
 * <p> {@link } and {@link } </p> *
 *
 * @author wonukHwang
 * @version 1.0
 * @see
 * @since (ex : 5 + 5)
 */
@Configuration
public class Resilience4JConfig {

  @Bean
  public Customizer<Resilience4JCircuitBreakerFactory> globalCustomConfiguration() {
    CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
        // CircuitBreaker를 열지 결정하는 failure rate threshold percentage default : 50
        .failureRateThreshold(4)
        // circuitBreaker를 open한 상태를 유지하는 지속 시간을 의미 이 기간 이후에 half-open 상태
        // default : 60 seconds
        .waitDurationInOpenState(Duration.ofMillis(1000))
        // CircuitBreaker가 닫힐 때 통화 결과를 기록하는 데 사용되는 슬라이딩 창의 유형을 구성
        // 카운트 기반 또는 시간 기반
        .slidingWindowType(SlidingWindowType.COUNT_BASED)
        // CircuitBreaker가 닫힐 때 호출 결과를 기록하는데 사용되는 슬라이딩 창의 크기를 구성
        // default : 100
        .slidingWindowSize(2)
        .build();

    TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom()
        .timeoutDuration(Duration.ofSeconds(4))
        .build();

    return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
          .timeLimiterConfig(timeLimiterConfig)
          .circuitBreakerConfig(circuitBreakerConfig)
          .build());
  }
}
