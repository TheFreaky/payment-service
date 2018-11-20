package ru.kpfu.itis.service;

import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.dto.PaymentDto;
import ru.kpfu.itis.model.Payment;

import java.time.Duration;
import java.util.Date;

@Service
public class PaymentService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MessageConverter messageConverter;

    public void save(Integer paymentInterval, Payment payment) {
        PaymentDto paymentDto = PaymentDto.builder()
                .contractId(payment.getContractId())
                .build();
        long delay = Duration.between(payment.getNextPaid().toInstant(), new Date().toInstant()).toMillis();
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setExpiration(String.valueOf(delay));

        String routingKey;
        if (paymentInterval < 20) {
            routingKey = "min";
        } else if (paymentInterval == 20) {
            routingKey = "hour";
        } else {
            routingKey = "day";
        }

        rabbitTemplate.send("front",routingKey, messageConverter.toMessage(paymentDto, messageProperties));
    }
}
