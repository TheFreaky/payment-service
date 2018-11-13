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
    private final static String DELAY_QUEUE_NAME = "delayed.queue";
    private final static String DELAY_EXCHANGE_NAME = "delayed.exchange";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MessageConverter messageConverter;

    public void save(Payment payment) {
        PaymentDto paymentDto = PaymentDto.builder()
                .contractId(payment.getContractId())
                .build();
        long delay = Duration.between(payment.getNextPaid().toInstant(), new Date().toInstant()).toMillis();
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setDelay((int) delay);
        rabbitTemplate.send(
                DELAY_EXCHANGE_NAME,
                DELAY_QUEUE_NAME,
                messageConverter.toMessage(paymentDto, messageProperties)
        );
    }
}
