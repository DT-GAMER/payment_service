package com.payment_example.payments.dto;

import java.math.BigDecimal;

public class PaymentRequest {
    private String type;
    private BigDecimal amount;
    private String currency;

    public PaymentRequest() {}
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}
