package com.rinhabackend.antonio.gateway.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AdminPaymentSummaryResponse(BigDecimal totalRequests,BigDecimal totalAmount,BigDecimal totalFee,BigDecimal feePerTransaction) {}
