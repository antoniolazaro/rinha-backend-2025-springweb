package com.rinhabackend.antonio;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;

public record PaymentPaymentProcessorRequest(String correlationId,
                                             BigDecimal amount,
                                             String requestedAt) {}

