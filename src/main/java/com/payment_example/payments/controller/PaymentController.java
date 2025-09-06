package com.payment_example.payments.controller;

import com.payment_example.payments.dto.PaymentRequest;
import com.payment_example.payments.model.Transaction;
import com.payment_example.payments.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    // --- Root endpoint (so / doesn't give Whitelabel error) ---
    @GetMapping("/")
    public Map<String, String> root() {
        Map<String, String> resp = new HashMap<>();
        resp.put("status", "UP");
        resp.put("message", "Payment Service is running \u2705");
        resp.put("hint", "Use POST /api/payments to create a transaction");
        return resp;
    }

    // --- Create a payment ---
    @PostMapping("/api/payments")
    public ResponseEntity<Transaction> create(
            @RequestHeader("X-API-KEY") String apiKey,
            @RequestHeader(value = "Idempotency-Key", required = false) String idemKey,
            @RequestBody PaymentRequest req) {

        Transaction tx = service.process(
                apiKey,
                req.getType(),
                req.getAmount(),
                req.getCurrency(),
                idemKey
        );
        return ResponseEntity.ok(tx);
    }

    // --- Get a payment by ID ---
    @GetMapping("/api/payments/{id}")
    public ResponseEntity<Transaction> get(@PathVariable UUID id) {
        return service.get(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
