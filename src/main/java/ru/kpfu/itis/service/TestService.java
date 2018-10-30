package ru.kpfu.itis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.model.Contract;
import ru.kpfu.itis.repository.ContractRepository;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 30.10.18
 *
 * @author Kuznetsov Maxim
 */
@Service
public class TestService {

    @Autowired
    private ContractRepository contractRepository;

    private ExecutorService executor = Executors.newFixedThreadPool(8);

    public void test() {
        Random random = new Random();
        List<Integer> paymentIntervals = Arrays.asList(15, 24 * 60, 30 * 24 * 60);

        for (int j = 0; j < 300000; j++) {
            executor.submit(() -> {
                contractRepository.save(Contract.builder()
                        .created(new Date())
                        .userId(random.nextLong())
                        .paymentInterval(paymentIntervals.get(random.nextInt(3)))
                        .build());
            });


        }
        System.out.println("End");
    }
}
