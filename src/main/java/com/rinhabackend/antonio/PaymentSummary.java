package com.rinhabackend.antonio;

import java.math.BigDecimal;

public record PaymentSummary(long totalRequests, BigDecimal totalAmount) {}

