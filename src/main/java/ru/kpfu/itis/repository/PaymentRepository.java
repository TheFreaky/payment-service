package ru.kpfu.itis.repository;

import ru.kpfu.itis.model.Payment;

/**
 * 30.10.18
 *
 * @author Kuznetsov Maxim
 */
public interface PaymentRepository {
    void save(Payment payment);
}
