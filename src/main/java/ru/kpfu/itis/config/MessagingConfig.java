package ru.kpfu.itis.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.kpfu.itis.config.property.MessagingProperties;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class MessagingConfig {

    @Autowired
    private MessagingProperties messagingProperties;

    @Bean
    public Exchange frontExchange() {
        return new DirectExchange("front");
    }

    @Bean
    public FanoutExchange backExchange() {
        return new FanoutExchange("back");
    }

    @Bean
    public Queue backQueue() {
        return new Queue("end-queue");
    }

    @Bean
    public Queue tenMinQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "");
        arguments.put("x-dead-letter-routing-key", "result");
        return new Queue("10min", true, false, false, arguments);
    }

    @Bean
    public Queue hourQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "");
        arguments.put("x-dead-letter-routing-key", "result");
        return new Queue("1hour", true, false, false, arguments);
    }

    @Bean
    public Queue dayQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "");
        arguments.put("x-dead-letter-routing-key", "result");
        return new Queue("1day", true, false, false, arguments);
    }

    @Bean
    public Binding bindingBack() {
        return BindingBuilder.bind(backQueue()).to(frontExchange()).with("result").noargs();
    }

    @Bean
    public Binding binding1() {
        return BindingBuilder.bind(tenMinQueue()).to(frontExchange()).with("min").noargs();
    }

    @Bean
    public Binding binding2() {
        return BindingBuilder.bind(hourQueue()).to(frontExchange()).with("hour").noargs();
    }

    @Bean
    public Binding binding3() {
        return BindingBuilder.bind(dayQueue()).to(frontExchange()).with("day").noargs();
    }

    @Bean
    public MessageConverter consumerMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
