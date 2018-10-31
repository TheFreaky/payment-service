package ru.kpfu.itis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.dto.ContractDto;
import ru.kpfu.itis.model.Contract;
import ru.kpfu.itis.model.Payment;
import ru.kpfu.itis.repository.ContractRepository;

import javax.persistence.EntityNotFoundException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 30.10.18
 *
 * @author Kuznetsov Maxim
 */
@Service
public class ContractService {

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private PaymentService paymentService;

    private ExecutorService executor = Executors.newFixedThreadPool(8);

    private AtomicInteger count = new AtomicInteger();
    private AtomicLong time = new AtomicLong();

    public void save(ContractDto dto) {
        Date nextPaidTime = new Date(dto.getCreated().toInstant()
                .plus(dto.getPaymentInterval(), ChronoUnit.MINUTES)
                .toEpochMilli());

        Contract contract = Contract.builder()
                .id(UUID.randomUUID().toString())
                .created(dto.getCreated())
                .paymentInterval(dto.getPaymentInterval())
                .userId(dto.getUserId())
                .nextPaymentDate(nextPaidTime)
                .build();

        executor.submit(() -> contractRepository.save(contract));
//        contractRepository.save(contract);
        executor.submit(() -> paymentService.save(Payment.builder()
                .id(contract.getId())
                .nextPaid(nextPaidTime)
                .build()));
    }

    public void takePayment(String id) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contract with id[" + id + "] not found"));

        Date oldPamentDate = contract.getNextPaymentDate();
        Date nextPaidTime = new Date(oldPamentDate.toInstant()
                .plus(contract.getPaymentInterval(), ChronoUnit.MINUTES)
                .toEpochMilli());


        contract.setNextPaymentDate(nextPaidTime);
        executor.submit(() -> contractRepository.save(contract));
        executor.submit(() -> paymentService.save(Payment.builder()
                .id(contract.getId())
                .nextPaid(nextPaidTime)
                .build()));

        count.incrementAndGet();
        time.addAndGet(Duration.between(oldPamentDate.toInstant(), new Date().toInstant()).getSeconds());

        if (count.get() > 0 && count.get() % 1000 == 0) {
            System.out.println("Count " + count.get());
            System.out.println("Avr time: " + time.get() / count.get());
            System.out.println("________________________________");
        }
    }
}
