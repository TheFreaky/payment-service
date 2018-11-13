package ru.kpfu.itis.service;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.dto.PaymentDto;

@Component
public class RabbitExpirationMessageListener {
    private final static String DELAY_QUEUE_NAME = "delayed.queue";
    private final static String DELAY_EXCHANGE_NAME = "delayed.exchange";

    @Autowired
    private ContractService contractService;

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = DELAY_QUEUE_NAME),
            exchange = @Exchange(value = DELAY_EXCHANGE_NAME, delayed = "true"), key = DELAY_QUEUE_NAME))
    public void process(PaymentDto contract) {
        contractService.takePayment(contract.getContractId());
    }
}
