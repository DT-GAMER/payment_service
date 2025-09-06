package com.payment_example.payments.gateway;

import java.math.BigDecimal;
import org.springframework.stereotype.Component;

/**
 * This is just to simulates an external card gateway. For demo: any amount > 0 is approved.
 */
@Component
public class MockGateway {
    public boolean authorize(BigDecimal amount) {
        return amount != null && amount.compareTo(BigDecimal.ZERO) > 0;
    }
}
