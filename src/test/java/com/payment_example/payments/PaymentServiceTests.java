package com.payment_example.payments;

import com.payment_example.payments.service.PaymentService;
import com.payment_example.payments.gateway.MockGateway;
import com.payment_example.payments.model.Transaction;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

public class PaymentServiceTests {
    private final PaymentService service = new PaymentService(new MockGateway());

    @Test
    void testCardAuthorization() {
        Transaction tx = service.process("valid-api-key", "card", BigDecimal.valueOf(100), "NGN", "idem-1");
        assertEquals(Transaction.Status.AUTHORIZED, tx.getStatus());
    }

    @Test
    void testIdempotency() {
        Transaction tx1 = service.process("valid-api-key", "wallet", BigDecimal.TEN, "USD", "idem-2");
        Transaction tx2 = service.process("valid-api-key", "wallet", BigDecimal.TEN, "USD", "idem-2");
        assertEquals(tx1.getId(), tx2.getId());
    }

    @Test
    void testUnauthorized() {
        assertThrows(SecurityException.class, () ->
            service.process("bad-key", "wallet", BigDecimal.ONE, "USD", null));
    }
}