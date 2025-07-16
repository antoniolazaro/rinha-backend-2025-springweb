package com.rinhabackend.antonio;

import java.io.Serializable;

public record ServiceHealthResponse(boolean failing, long minResponseTime) implements Serializable {}
