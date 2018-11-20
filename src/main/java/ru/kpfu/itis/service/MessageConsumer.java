package ru.kpfu.itis.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.dto.ContractDto;

@Component
public class MessageConsumer {

    @Autowired
    private ContractService contractService;

    @RabbitListener(queues = "${messaging.contract-create.queue}")
    public void process(ContractDto dto) {
        contractService.save(dto);
    }
}
