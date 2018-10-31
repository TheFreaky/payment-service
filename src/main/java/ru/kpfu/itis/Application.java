package ru.kpfu.itis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.kpfu.itis.config.property.MessagingProperties;

@SpringBootApplication
@EnableConfigurationProperties(MessagingProperties.class)
public class Application {
    public static void main(String... args) {
        SpringApplication.run(Application.class, args);
    }

}
