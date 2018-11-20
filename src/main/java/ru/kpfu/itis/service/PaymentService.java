package ru.kpfu.itis.service;

import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.config.property.MessagingProperties;
import ru.kpfu.itis.dto.PaymentDto;
import ru.kpfu.itis.model.Payment;
import ru.kpfu.itis.model.PaymentInterval;

import java.time.Duration;
import java.util.Date;

@Service
public class PaymentService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MessageConverter messageConverter;

    @Autowired
    private MessagingProperties messagingProperties;

    public void save(PaymentInterval paymentInterval, Payment payment) {
        PaymentDto paymentDto = PaymentDto.builder()
                .contractId(payment.getContractId())
                .build();
        long delay = Duration.between(new Date().toInstant(), payment.getNextPaid().toInstant()).toMillis();
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setExpiration(String.valueOf(delay));
        messageProperties.getHeaders().put("expiration", String.valueOf(delay));

        String routingKey;
        if (paymentInterval.equals(PaymentInterval.FAST)) {
            routingKey = messagingProperties.getFastContract().getRoutingKey();
        } else if (paymentInterval.equals(PaymentInterval.NORMAL)) {
            routingKey = messagingProperties.getNormalContract().getRoutingKey();
        } else {
            routingKey = messagingProperties.getSlowContract().getRoutingKey();
        }

        rabbitTemplate.send(messagingProperties.getExchange(), routingKey, messageConverter.toMessage(paymentDto, messageProperties));
    }
}
