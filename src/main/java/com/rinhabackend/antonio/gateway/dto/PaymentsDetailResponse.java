package com.rinhabackend.antonio.gateway.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentsDetailResponse(String correlationId, BigDecimal amount, LocalDateTime requeustedAt) {}
