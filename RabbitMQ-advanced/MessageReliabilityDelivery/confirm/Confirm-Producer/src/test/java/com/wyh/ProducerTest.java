package com.wyh;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.connection.CorrelationData;
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
     * confirm 确认模式
     * 1.开启确认模式（connectionFactory中开启confirm-type="CORRELATED"
     * 2.使用RabbitTemplate定义confirmCallback回调函数
     */
    @Test
    public void Confirm() {
        //定义回调
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            /**
             * ConfirmCallback的confirm方法参数
             * @param correlationData：相关配置信息
             * @param b - ack：交换机是否接收到了消息
             * @param s - cause：交换机接收失败的原因
             */
            @Override
            public void confirm(CorrelationData correlationData, boolean b, String s) {
                System.out.println("This is the ConfirmCallback");
                if (!b) {
                    System.out.println("failed cause: " + s);
                    //失败后消息处理 比如再次发送消息
                }
            }
        });
        //发送消息
        for (int i = 0; i < 10; i++) {
            rabbitTemplate.convertAndSend("Confirm-direct-exchange","wyh.confirm","iWyh2-Confirm");
        }
    }
}
