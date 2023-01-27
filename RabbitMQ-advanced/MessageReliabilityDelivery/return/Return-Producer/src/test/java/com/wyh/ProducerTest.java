package com.wyh;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Message;
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
     * return 回退模式
     * 1.开启回退模式（connectionFactory中开启publisher-returns="true"
     * 2.设置交换机Exchange处理消息的模式：
     *                               ① 如果消息没有路由到Queue，则丢弃消息（默认处理）
     *                               ② 如果消息没有路由到Queue，返回给消息发送方的ReturnCallback
     * 3.设置ReturnCallback
     */
    @Test
    public void Return() {
        //2.设置交换机处理消息失败之后，将消息返回给发送方，ReturnCallback中就可以获取这条返回的消息
        rabbitTemplate.setMandatory(true);
        //3.设置ReturnCallback
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            /**
             * ReturnCallback的returnedMessage方法参数
             * @param message 消息对象
             * @param i - replyCode：错误码
             * @param s - replyText：错误信息
             * @param s1 - exchange：交换机名称
             * @param s2 - routingKey：消息的路由键
             */
            @Override
            public void returnedMessage(Message message, int i, String s, String s1, String s2) {
                System.out.println("This is the ReturnCallback");
                System.out.println("message: "+new String(message.getBody()));
                System.out.println("replyCode: "+i);
                System.out.println("replyText: "+s);
                System.out.println("exchange: "+s1);
                System.out.println("routingKey: "+s2);
                //对消息的二次处理
            }
        });
        //4.发送消息 - RoutingKey错误，将会失败
        rabbitTemplate.convertAndSend("Return-direct-exchange","wyh.return.confirm","iWyh2-Return");
    }
}
