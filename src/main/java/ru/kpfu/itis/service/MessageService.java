package ru.kpfu.itis.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.config.property.MessagingProperties;
import ru.kpfu.itis.dto.ContractDto;

@Service
public class MessageService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MessagingProperties messagingProperties;

    public void process(ContractDto dto) {
        rabbitTemplate.convertAndSend(messagingProperties.getExchange(),
                messagingProperties.getContract().getRoutingKey(), dto);
    }
}
