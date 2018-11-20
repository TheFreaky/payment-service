package ru.kpfu.itis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.dto.ContractDto;
import ru.kpfu.itis.model.Contract;
import ru.kpfu.itis.model.Payment;
import ru.kpfu.itis.repository.ContractRepository;

import javax.persistence.EntityNotFoundException;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    public void save(ContractDto dto) {
        Date nextPaidTime = new Date(dto.getCreated().toInstant()
//                FIXME
//                .plus(dto.getPaymentInterval(), ChronoUnit.MINUTES)
                .plus(dto.getPaymentInterval(), ChronoUnit.SECONDS)
                .toEpochMilli());

        Contract contract = Contract.builder()
                .id(UUID.randomUUID().toString())
                .created(dto.getCreated())
                .paymentInterval(dto.getPaymentInterval())
                .userId(dto.getUserId())
                .nextPaymentDate(nextPaidTime)
                .build();

        //FIXME
        System.out.println(contract);
        System.out.println("________________________________");

        executor.submit(() -> contractRepository.save(contract));
        executor.submit(() -> paymentService.save(contract.getPaymentInterval(), Payment.builder()
                .contractId(contract.getId())
                .nextPaid(nextPaidTime)
                .build()));
    }

    public void takePayment(String id) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contract with id[" + id + "] not found"));

        //FIXME
        System.out.println("Payment. Id: " + id);
        System.out.println("Date: " + new Date());
        System.out.println("________________________________");

        Date oldPamentDate = contract.getNextPaymentDate();
        Date nextPaidTime = new Date(oldPamentDate.toInstant()
//                FIXME
//                .plus(contract.getPaymentInterval(), ChronoUnit.MINUTES)
                .plus(contract.getPaymentInterval(), ChronoUnit.SECONDS)
                .toEpochMilli());


        contract.setNextPaymentDate(nextPaidTime);
        executor.submit(() -> contractRepository.save(contract));
        executor.submit(() -> paymentService.save(contract.getPaymentInterval(), Payment.builder()
                .contractId(contract.getId())
                .nextPaid(nextPaidTime)
                .build()));
    }
}
