package com.rinhabackend.antonio;

import com.rinhabackend.antonio.gateway.PaymentProcessorDefaultGateway;
import com.rinhabackend.antonio.gateway.PaymentProcessorFallbackGateway;
import com.rinhabackend.antonio.gateway.PaymentProcessorGateway;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentProcessorComponent {

    private final PaymentProcessorDefaultGateway defaultGateway;
    private final PaymentProcessorFallbackGateway fallbackGateway;
    private final RedisTemplate<String, ServiceHealthResponse> serviceHealthResponseRedisTemplate;

    private static final String DEFAULT_HEALTH_KEY = "health:default";
    private static final String FALLBACK_HEALTH_KEY = "health:fallback";
    private static final Duration DURATION_DEFAULT = Duration.ofMillis(5500);

    @PostConstruct
    void init() {
        updateHealthCheckPaymentProcessorDefault();
        updateHealthCheckPaymentProcessorFallback();
    }

    private ServiceHealthResponse updateHealthCheckPaymentProcessorDefault() {
        return updateHealthCheckPaymentProcessor(defaultGateway, DEFAULT_HEALTH_KEY);
    }

    private ServiceHealthResponse updateHealthCheckPaymentProcessorFallback() {
        return updateHealthCheckPaymentProcessor(fallbackGateway, FALLBACK_HEALTH_KEY);
    }

    //TODO posso ganhar tempo colocando isso em outro bean e botando para ser async?
    private ServiceHealthResponse updateHealthCheckPaymentProcessor(PaymentProcessorGateway paymentProcessorGateway, String cacheKey) {
        var serviceHealth = paymentProcessorGateway.getServiceHealth();
        serviceHealthResponseRedisTemplate.opsForValue().set(cacheKey, serviceHealth, DURATION_DEFAULT);
        return serviceHealth;
    }

    public PaymentProcessorGateway providedAvailablePaymentProcessorGateway() {
        var defaultHealth = paymentProcessorHealth(DEFAULT_HEALTH_KEY, defaultGateway);
        var fallbackHealth = paymentProcessorHealth(FALLBACK_HEALTH_KEY, fallbackGateway);


        boolean defaultAvailable = !defaultHealth.failing();
        boolean fallbackAvailable = !fallbackHealth.failing();

        if (defaultAvailable) {
            return defaultGateway;
        } else if (fallbackAvailable) {
            return fallbackGateway;
        } else {
            var minResponseTimeDefault = defaultHealth.minResponseTime();
            var minResponseTimeFallback = fallbackHealth.minResponseTime();
            if (minResponseTimeDefault <= minResponseTimeFallback) {
                //TODO publicar com delay
                return defaultGateway;
            } else {
                //TODO publicar com delay
                return fallbackGateway;
            }
        }
    }


    private ServiceHealthResponse paymentProcessorHealth(String key, PaymentProcessorGateway paymentProcessorGateway) {
        var value =  serviceHealthResponseRedisTemplate.opsForValue().get(key);
        if(Objects.nonNull(value)){
            return value;
        }
        if(paymentProcessorGateway.isDefault()){
            return updateHealthCheckPaymentProcessorDefault();
        }
        return updateHealthCheckPaymentProcessorFallback();
    }
}
