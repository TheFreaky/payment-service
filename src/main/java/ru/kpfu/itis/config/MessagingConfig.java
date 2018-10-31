package ru.kpfu.itis.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.kpfu.itis.config.property.MessagingProperties;

@Configuration
public class MessagingConfig {

    @Autowired
    private MessagingProperties messagingProperties;

    @Bean
    public Exchange exchange() {
        return new TopicExchange(messagingProperties.getExchange());
    }

    @Bean
    public Queue queue() {
        return new Queue(messagingProperties.getContract().getQueue());
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(exchange()).with(messagingProperties.getContract().getRoutingKey()).noargs();
    }

    @Bean
    public MessageConverter consumerMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
