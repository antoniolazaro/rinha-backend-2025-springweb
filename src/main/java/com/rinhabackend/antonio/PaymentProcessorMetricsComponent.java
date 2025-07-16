package com.rinhabackend.antonio;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentProcessorMetricsComponent {

    private final RedisTemplate<String, PaymentPaymentProcessorRequest> logRedisTemplate;

    private static final String LOG_DEFAULT_KEY = "log:default";
    private static final String LOG_FALLBACK_KEY = "log:fallback";

    public void writeDefaultLog(PaymentPaymentProcessorRequest paymentPaymentProcessorRequest) {
        logRedisTemplate.opsForList().rightPush(LOG_DEFAULT_KEY,paymentPaymentProcessorRequest);
    }
    public void writeFallbackLog(PaymentPaymentProcessorRequest paymentPaymentProcessorRequest) {
        logRedisTemplate.opsForList().rightPush(LOG_FALLBACK_KEY,paymentPaymentProcessorRequest);
    }
    public List<PaymentPaymentProcessorRequest> readDefaultLog() {
        return logRedisTemplate.opsForList().range(LOG_DEFAULT_KEY,0,-1);
    }
    public List<PaymentPaymentProcessorRequest> readFallbackLog() {
        return logRedisTemplate.opsForList().range(LOG_FALLBACK_KEY,0,-1);
    }
    public void clear() {
        logRedisTemplate.opsForList().getOperations().delete(LOG_DEFAULT_KEY);
        logRedisTemplate.opsForList().getOperations().delete(LOG_FALLBACK_KEY);
    }
}
