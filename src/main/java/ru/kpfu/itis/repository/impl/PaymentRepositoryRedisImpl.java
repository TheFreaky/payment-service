package ru.kpfu.itis.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import ru.kpfu.itis.model.Payment;
import ru.kpfu.itis.repository.PaymentRepository;

@Repository
public class PaymentRepositoryRedisImpl implements PaymentRepository {

    private static final String KEY = "payment:";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void save(Payment payment) {
        String key = KEY + payment.getId();
        redisTemplate.opsForValue().set(key, "");
        redisTemplate.expireAt(key, payment.getNextPaid());
    }
}
