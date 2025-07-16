package com.rinhabackend.antonio;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class PaymentIntegratorController {

    private final PaymentIntegratorService paymentIntegratorService;

    @PostMapping("/payments")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void createPayment(@RequestBody @Valid PaymentRequest request) {
         paymentIntegratorService.pay(request);
    }

    @GetMapping("/payments-summary")
    @ResponseStatus(HttpStatus.OK)
    public PaymentSummaryResponse getSummary(
            @RequestParam Instant from,
            @RequestParam Instant to) {
        return paymentIntegratorService.getSummary(from, to);
    }

    @PostMapping("/purge-payments")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void purge() {
        paymentIntegratorService.purge();
    }
}
