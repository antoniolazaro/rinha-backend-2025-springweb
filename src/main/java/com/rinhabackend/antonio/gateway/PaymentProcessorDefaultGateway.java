package com.rinhabackend.antonio.gateway;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "paymentProcessorDefaultGateway", url = "${payment.processor.url.default}")
public interface PaymentProcessorDefaultGateway extends PaymentProcessorGateway {

    @Override
    default Boolean isDefault() {
        return true;
    }
}

