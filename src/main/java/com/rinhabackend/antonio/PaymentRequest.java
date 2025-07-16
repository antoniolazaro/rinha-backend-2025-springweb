package com.rinhabackend.antonio;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;

public record PaymentRequest(@NotBlank(message = "Campo obrigatório") String correlationId,
                             @NotNull(message = "Campo obrigatório") BigDecimal amount) {}

