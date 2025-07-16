package com.rinhabackend.antonio.gateway;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "PaymentProcessorFallbacktGateway", url = "${payment.processor.url.fallback}")
public interface PaymentProcessorFallbackGateway extends PaymentProcessorGateway {

    @Override
    default Boolean isDefault() {
        return false;
    }
}