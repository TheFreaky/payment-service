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

    private static final String EXPIRED_MESSAGE_ROUTING_KEY = "x-dead-letter-routing-key";
    private static final String EXPIRED_MESSAGE_EXCHANGE = "x-dead-letter-exchange";

    @Autowired
    private MessagingProperties messagingProperties;

    @Bean
    public Exchange expiryExchange() {
        return new DirectExchange(messagingProperties.getExchange());
    }

    @Bean
    public Queue paymentWithdrawQueue() {
        return new Queue(messagingProperties.getPaymentWithdraw().getQueue());
    }

    @Bean
    public Queue slowContractQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put(EXPIRED_MESSAGE_EXCHANGE, "");
        arguments.put(EXPIRED_MESSAGE_ROUTING_KEY, messagingProperties.getPaymentWithdraw().getQueue());
        return new Queue(messagingProperties.getSlowContract().getQueue(), true, false, false, arguments);
    }

    @Bean
    public Queue normalContractQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put(EXPIRED_MESSAGE_EXCHANGE, "");
        arguments.put(EXPIRED_MESSAGE_ROUTING_KEY, messagingProperties.getPaymentWithdraw().getQueue());
        return new Queue(messagingProperties.getNormalContract().getQueue(), true, false, false, arguments);
    }

    @Bean
    public Queue fastContractQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put(EXPIRED_MESSAGE_EXCHANGE, "");
        arguments.put(EXPIRED_MESSAGE_ROUTING_KEY, messagingProperties.getPaymentWithdraw().getQueue());
        return new Queue(messagingProperties.getFastContract().getQueue(), true, false, false, arguments);
    }

    @Bean
    public Binding binding1() {
        return BindingBuilder.bind(slowContractQueue()).to(expiryExchange()).with(messagingProperties.getSlowContract().getRoutingKey()).noargs();
    }

    @Bean
    public Binding binding2() {
        return BindingBuilder.bind(normalContractQueue()).to(expiryExchange()).with(messagingProperties.getNormalContract().getRoutingKey()).noargs();
    }

    @Bean
    public Binding binding3() {
        return BindingBuilder.bind(fastContractQueue()).to(expiryExchange()).with(messagingProperties.getFastContract().getRoutingKey()).noargs();
    }

    @Bean
    public MessageConverter consumerMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
