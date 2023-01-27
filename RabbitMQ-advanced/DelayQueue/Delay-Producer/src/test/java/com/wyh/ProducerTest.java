package com.wyh;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-rabbitmq-producer.xml"})
public class ProducerTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Test
    public void DelayQueue() {
        //模拟下单成功
        System.out.println("You have successfully placed your order");
        //模拟三十分钟内用户任未支付
        //发送确认订单状态消息
        rabbitTemplate.convertAndSend("OrderNormalExchange","wyh.order.ack","Whether the order is paid or not?");
    }
}
