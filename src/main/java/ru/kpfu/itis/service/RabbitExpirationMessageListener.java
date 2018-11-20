package ru.kpfu.itis.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.dto.PaymentDto;

@Component
public class RabbitExpirationMessageListener {
    @Autowired
    private ContractService contractService;

    @RabbitListener(queues = "end-queue")
    public void process(PaymentDto contract) {
        contractService.takePayment(contract.getContractId());
    }
}
