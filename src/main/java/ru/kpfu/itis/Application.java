package ru.kpfu.itis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import ru.kpfu.itis.config.property.MessagingProperties;
import ru.kpfu.itis.dto.ContractDto;
import ru.kpfu.itis.service.MessageService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
@EnableConfigurationProperties(MessagingProperties.class)
public class Application {
    public static void main(String... args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        MessageService messageService = context.getBean(MessageService.class);
        Random random = new Random();
        List<Integer> intervals = Arrays.asList(1,3,5);
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        for (int i = 0; i < 50000000; i++) {
            executorService.submit(() ->
                    messageService.process(ContractDto.builder()
                            .userId(random.nextLong())
                            .paymentInterval(intervals.get(random.nextInt(3)))
                            .created(new Date())
                            .build()));
        }
        System.out.println("All");

    }

}
