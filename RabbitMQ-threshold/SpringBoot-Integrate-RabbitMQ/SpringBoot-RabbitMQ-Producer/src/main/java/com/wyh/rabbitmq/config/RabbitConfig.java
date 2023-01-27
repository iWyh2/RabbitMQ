package com.wyh.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ配置类
 * 配置：
 * 1.Exchange交换机
 * 2.Queue队列
 * 3.Binding绑定关系
 */
@Configuration
public class RabbitConfig {
    //1.Exchange交换机
    @Bean("topicExchange")
    public Exchange TopicExchange() {
        return ExchangeBuilder.topicExchange("bootTopicExchange")
                .durable(true)
                .build();
    }
    @Bean("topicTTLExchange")
    public Exchange TopicTTLExchange() {
        return ExchangeBuilder.topicExchange("bootTTLTopicExchange")
                .durable(true)
                .build();
    }
    @Bean("NormalExchange")
    public Exchange NormalExchange() {
        return ExchangeBuilder.directExchange("NormalExchange")
                .durable(true)
                .build();
    }
    @Bean("DeadLetterExchange")
    public Exchange DeadLetterExchange() {
        return ExchangeBuilder.directExchange("DeadLetterExchange")
                .durable(true)
                .build();
    }
    @Bean("ServiceNormalExchange")
    public Exchange ServiceNormalExchange() {
        return ExchangeBuilder.topicExchange("ServiceNormalExchange")
                .durable(true)
                .build();
    }
    @Bean("ServiceDeadLetterExchange")
    public Exchange ServiceDeadLetterExchange() {
        return ExchangeBuilder.topicExchange("ServiceDeadLetterExchange")
                .durable(true)
                .build();
    }

    //2.Queue队列
    @Bean("topicErrorQueue")
    public Queue TopicErrorQueue() {
        return QueueBuilder.durable("bootTopicErrorQueue").build();
    }
    @Bean("topicInfoQueue")
    public Queue TopicInfoQueue() {
        return QueueBuilder.durable("bootTopicInfoQueue").build();
    }
    @Bean("topicTTLQueue")
    public Queue TopicTTLQueue() {
        //给队列设置TTL
        return QueueBuilder.durable("bootTopicTTLQueue").ttl(5000).build();
    }
    /**
     * 与死信交换机绑定的普通队列
     */
    @Bean("NormalQueue")
    public Queue NormalQueue() {
        return QueueBuilder
                .durable("NormalQueue")
                .ttl(10000)//设置队列的TTL
                .maxLength(10)//设置队列最大长度 超出了长度就成了死信
                .deadLetterExchange("DeadLetterExchange")//指定与哪个死信交换机绑定
                .deadLetterRoutingKey("wyh.dlx.info")//该死信交换机与死信队列绑定的RoutingKey 在下面绑定
                .build();
    }
    @Bean("DeadLetterQueue")
    public Queue DeadLetterQueue() {
        return QueueBuilder.durable("DeadLetterQueue").build();
    }
    /**
     * 延迟队列模拟
     */
    @Bean("ServiceNormalQueue")
    public Queue ServiceNormalQueue() {
        return QueueBuilder.durable("ServiceNormalQueue")
                .ttl(10000)
                .deadLetterExchange("ServiceDeadLetterExchange")
                .deadLetterRoutingKey("wyh.dlx.service.ack")
                .build();
    }
    @Bean("ServiceDeadLetterQueue")
    public Queue ServiceDeadLetterQueue() {
        return QueueBuilder.durable("ServiceDeadLetterQueue").build();
    }

    //3.Binding绑定关系
    @Bean
    public Binding BindingError(@Qualifier("topicErrorQueue") Queue queue, @Qualifier("topicExchange")Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("*.wyh.error").noargs();
    }
    @Bean
    public Binding BindingError_Info_Exception(@Qualifier("topicErrorQueue") Queue queue, @Qualifier("topicExchange")Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("*.wyh.info.exception").noargs();
    }
    @Bean
    public Binding BindingInfo(@Qualifier("topicInfoQueue") Queue queue, @Qualifier("topicExchange")Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("*.wyh.info.*").noargs();
    }
    @Bean
    public Binding BindingTTL(@Qualifier("topicTTLQueue") Queue queue, @Qualifier("topicTTLExchange")Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("wyh.ttl.#").noargs();
    }
    /**
     * 绑定死信队列和死信交换机
     * @param queue 上面定义的死信队列
     * @param exchange 上面定义的死信交换机
     */
    @Bean("dlx")
    public Binding DLXBinding(@Qualifier("DeadLetterQueue")Queue queue, @Qualifier("DeadLetterExchange")Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("wyh.dlx.info").noargs();
    }
    @Bean("normal")
    public Binding NormalBinding(@Qualifier("NormalQueue")Queue queue, @Qualifier("NormalExchange")Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("wyh.normal.info").noargs();
    }

    @Bean
    public Binding ServiceBinding(@Qualifier("ServiceNormalQueue")Queue queue, @Qualifier("ServiceNormalExchange")Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("wyh.service.#").noargs();
    }
    @Bean
    public Binding ServiceDlxBinding(@Qualifier("ServiceDeadLetterQueue")Queue queue, @Qualifier("ServiceDeadLetterExchange")Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("wyh.dlx.service.#").noargs();
    }
}
