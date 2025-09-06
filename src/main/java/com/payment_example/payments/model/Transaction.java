package com.payment_example.payments.model;

import java.math.BigDecimal;
import java.util.UUID;

public class Transaction {
    public enum Status { PENDING, AUTHORIZED, COMPLETED, FAILED }

    private UUID id;
    private String type; // card, bank_transfer, wallet
    private BigDecimal amount;
    private String currency;
    private Status status;
    private String idempotencyKey;

    public Transaction() {}

    public Transaction(UUID id, String type, BigDecimal amount,
                       String currency, Status status, String idempotencyKey) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.idempotencyKey = idempotencyKey;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public String getIdempotencyKey() { return idempotencyKey; }
    public void setIdempotencyKey(String idempotencyKey) { this.idempotencyKey = idempotencyKey; }
}
