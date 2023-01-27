package com.wyh;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-rabbitmq-producer.xml")
public class ProducerTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 测试死信：
     * 1.消息过期
     * 2.超过队列长度
     * 3.消费端拒收
     */
    @Test
    public void DLX() {
        //1.消息过期
        //rabbitTemplate.convertAndSend("NormalExchange","wyh.info","iWyh2-王钇欢YYDS");
        //2.超过队列长度
        for (int i = 0; i < 15; i++) {
            rabbitTemplate.convertAndSend("NormalExchange","wyh.info","iWyh2-王钇欢YYDS");
        }
        //3.消费者拒收消息
        //消费者监听NormalQueue的消息，并对其中的消息拒绝接受，且消息不重新回到该队列
    }
}
