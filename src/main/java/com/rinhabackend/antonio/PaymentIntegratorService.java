package com.rinhabackend.antonio;

import com.rinhabackend.antonio.gateway.PaymentProcessorDefaultGateway;
import com.rinhabackend.antonio.gateway.PaymentProcessorFallbackGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentIntegratorService {

    private final PaymentProcessorDefaultGateway defaultGateway;
    private final PaymentProcessorFallbackGateway fallbackGateway;
    private final PaymentProcessorComponent paymentProcessorComponent;
    private final PaymentProcessorMetricsComponent paymentProcessorMetricsComponent;

    @Async
    public void pay(PaymentRequest request) {
        PaymentPaymentProcessorRequest requestProcessor = new PaymentPaymentProcessorRequest(
                request.correlationId(),
                request.amount(),
                DateTimeFormatter.ISO_INSTANT.format(Instant.now())
        );
        var paymentProcessorGateway =  paymentProcessorComponent.providedAvailablePaymentProcessorGateway();
        paymentProcessorGateway.createPayment(requestProcessor);
        var isDefault = paymentProcessorGateway.isDefault();
        if(isDefault){
            paymentProcessorMetricsComponent.writeDefaultLog(requestProcessor);
        }else{
            paymentProcessorMetricsComponent.writeFallbackLog(requestProcessor);
        }
    }

    public PaymentSummaryResponse getSummary(Instant from, Instant to) {

        var defaultLog = paymentProcessorMetricsComponent.readDefaultLog().stream().filter(
                item -> !Instant.parse(item.requestedAt()).isBefore(from) &&
                        !Instant.parse(item.requestedAt()).isAfter(to)).toList();

        var fallBackLog = paymentProcessorMetricsComponent.readFallbackLog().stream().filter(
                item -> !Instant.parse(item.requestedAt()).isBefore(from) &&
                        !Instant.parse(item.requestedAt()).isAfter(to)).toList();

var totalDefault = defaultLog.stream().map(PaymentPaymentProcessorRequest::amount).reduce(BigDecimal.ZERO,BigDecimal::add);
        var totalFallback = fallBackLog.stream().map(PaymentPaymentProcessorRequest::amount).reduce(BigDecimal.ZERO,BigDecimal::add);
        return new PaymentSummaryResponse(new PaymentSummary(defaultLog.size(),totalDefault),new PaymentSummary(fallBackLog.size(), totalFallback));

    }

    public void purge() {
        final String token = "123"; // TODO: Parametrizar
        defaultGateway.purgePayments(token);
        fallbackGateway.purgePayments(token);
        paymentProcessorMetricsComponent.clear();
    }
}
