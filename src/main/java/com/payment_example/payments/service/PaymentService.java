package com.payment_example.payments.service;

import com.payment_example.payments.model.Transaction;
import com.payment_example.payments.gateway.MockGateway;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class PaymentService {
    private final Map<UUID, Transaction> store = new LinkedHashMap<>();
    private final Set<String> idempotencyKeys = new HashSet<>();
    private final MockGateway gateway;

    public PaymentService(MockGateway gateway) {
        this.gateway = gateway;
    }

    public synchronized Transaction process(String apiKey, String type,
                                            BigDecimal amount, String currency,
                                            String idemKey) {


        // This is just a a basic security check (demo), for a real system I will uses OAuth2/JWT
        if (!"valid-api-key".equals(apiKey)) {
            throw new SecurityException("Unauthorized");
        }

        if (idemKey != null && idempotencyKeys.contains(idemKey)) {
            return store.values().stream()
                .filter(t -> idemKey.equals(t.getIdempotencyKey()))
                .findFirst().orElse(null);
        }

        UUID id = UUID.randomUUID();
        Transaction tx = new Transaction(id, type, amount, currency,
                                         Transaction.Status.PENDING, idemKey);
        store.put(id, tx);
        if (idemKey != null) idempotencyKeys.add(idemKey);

        if ("card".equalsIgnoreCase(type) && gateway.authorize(amount)) {
            tx.setStatus(Transaction.Status.AUTHORIZED);
        } else if (!"card".equalsIgnoreCase(type)) {
            tx.setStatus(Transaction.Status.COMPLETED);
        } else {
            tx.setStatus(Transaction.Status.FAILED);
        }

        store.put(id, tx);
        return tx;
    }

    // simple retrieval helper (for demo)
    public Optional<Transaction> get(UUID id) {
        return Optional.ofNullable(store.get(id));
    }
}
