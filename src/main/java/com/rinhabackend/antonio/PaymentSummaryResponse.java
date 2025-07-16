package com.rinhabackend.antonio;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PaymentSummaryResponse(@JsonProperty("default") PaymentSummary defaultss, PaymentSummary fallback) {}

