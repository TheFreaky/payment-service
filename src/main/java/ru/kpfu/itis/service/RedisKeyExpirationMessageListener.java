package ru.kpfu.itis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class RedisKeyExpirationMessageListener implements MessageListener {

    @Autowired
    private ContractService contractService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String body = message.toString();
        String id = body.substring("payment:".length());
        contractService.takePayment(id);
    }
}
