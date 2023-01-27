package com.wyh;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-rabbitmq-producer.xml")
public class ProducerTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;//1.注入RabbitTemplate

    /**
     * 简单模式
     */
    @Test
    public void SimpleMode() {
        //2.发送消息
        rabbitTemplate.convertAndSend("spring-queue","Change The World!");
    }

    /**
     * 工作模式
     */
    @Test
    public void WorkQueues() {
        //2.发送消息
        rabbitTemplate.convertAndSend("spring-queue","Change The World!");
    }

    /**
     * 订阅模式
     */
    @Test
    public void pubSub() {
        //2.发送消息
        rabbitTemplate.convertAndSend("spring-fanout-exchange","","spring-fanout-queue-PubSub-iWyh2");
    }

    /**
     * 路由模式
     */
    @Test
    public void Routing() {
        //2.发送消息
        rabbitTemplate.convertAndSend("spring-direct-exchange","wyh.pubsub.info","spring-direct-exchange-Routing-iWyh2");
        rabbitTemplate.convertAndSend("spring-direct-exchange","wyh.pubsub.warning","spring-direct-exchange-Routing-iWyh2");
        rabbitTemplate.convertAndSend("spring-direct-exchange","wyh.pubsub.orders","spring-direct-exchange-Routing-iWyh2");
        rabbitTemplate.convertAndSend("spring-direct-exchange","wyh.pubsub.exception","spring-direct-exchange-Routing-iWyh2");
        rabbitTemplate.convertAndSend("spring-direct-exchange","wyh.pubsub.error","spring-direct-exchange-Routing-iWyh2");
    }

    /**
     * 话题模式
     */
    @Test
    public void Topics() {
        //2.发送消息
        rabbitTemplate.convertAndSend("spring-topic-exchange","wyh.info","spring-topic-exchange-Topics-iWyh2");
        rabbitTemplate.convertAndSend("spring-topic-exchange","wyh.info.info","spring-topic-exchange-Topics-iWyh2 will change the world!");
        rabbitTemplate.convertAndSend("spring-topic-exchange","System.wyh.info","spring-topic-exchange-Topics-iWyh2 will change the world!");
    }
}
