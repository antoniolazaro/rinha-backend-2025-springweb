package com.rinhabackend.antonio.gateway;

import com.rinhabackend.antonio.PaymentPaymentProcessorRequest;
import com.rinhabackend.antonio.ServiceHealthResponse;
import com.rinhabackend.antonio.gateway.dto.AdminPaymentSummaryResponse;
import com.rinhabackend.antonio.gateway.dto.DatabasePurgeResponse;
import com.rinhabackend.antonio.gateway.dto.DelayConfig;
import com.rinhabackend.antonio.gateway.dto.FailureConfig;
import com.rinhabackend.antonio.gateway.dto.PaymentsCreateResponse;
import com.rinhabackend.antonio.gateway.dto.PaymentsDetailResponse;
import com.rinhabackend.antonio.gateway.dto.TokenConfig;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Instant;

public interface PaymentProcessorGateway {

    Boolean isDefault();

    @GetMapping("/payments/service-health")
    ServiceHealthResponse getServiceHealth();

    @PostMapping("/payments")
    PaymentsCreateResponse createPayment(@RequestBody PaymentPaymentProcessorRequest request);

    @GetMapping("/payments/{id}")
    PaymentsDetailResponse getPayment(@PathVariable("id") String id);

    @GetMapping("/admin/payments-summary")
    AdminPaymentSummaryResponse getAdminSummary(
            @RequestHeader("X-Rinha-Token") String token,
            @RequestParam("from") Instant from,
            @RequestParam("to") Instant to
    );

    @PutMapping("/admin/configurations/token")
    void updateToken(
            @RequestHeader("X-Rinha-Token") String token,
            @RequestBody TokenConfig config
    );

    @PutMapping("/admin/configurations/delay")
    void updateDelay(
            @RequestHeader("X-Rinha-Token") String token,
            @RequestBody DelayConfig config
    );

    @PutMapping("/admin/configurations/failure")
    void updateFailure(
            @RequestHeader("X-Rinha-Token") String token,
            @RequestBody FailureConfig config
    );

    @PostMapping("/admin/purge-payments")
    DatabasePurgeResponse purgePayments(@RequestHeader("X-Rinha-Token") String token);
}

