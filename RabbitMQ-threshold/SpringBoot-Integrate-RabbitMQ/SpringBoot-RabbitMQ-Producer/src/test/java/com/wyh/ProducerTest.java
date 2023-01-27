package com.wyh;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProducerTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;//1.注入RabbitTemplate

    @Test
    public void Topics() {
        //2.发送消息
        rabbitTemplate.convertAndSend("bootTopicExchange","System.wyh.error","Topic-iWyh2-error");
        rabbitTemplate.convertAndSend("bootTopicExchange","System.wyh.info.exception","Topic-iWyh2-info-exception");
        rabbitTemplate.convertAndSend("bootTopicExchange","System.wyh.info.warning","Topic-iWyh2-info-warning");
        rabbitTemplate.convertAndSend("bootTopicExchange","System.wyh.info.orders","Topic-iWyh2-info-orders");
    }

    /**
     * confirm 确认模式
     * 1.开启确认模式
     * 2.使用RabbitTemplate定义confirmCallback回调函数
     */
    @Test
    public void Confirm() {
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
        rabbitTemplate.convertAndSend("Confirm-direct-exchange","wyh.confirm","iWyh2-Confirm");
    }

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
        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            @Override
            public void returnedMessage(ReturnedMessage returnedMessage) {
                System.out.println("This is the ReturnCallback");
                System.out.println("message: "+new String(returnedMessage.getMessage().getBody()));
                System.out.println("replyCode: "+returnedMessage.getReplyCode());
                System.out.println("replyText: "+returnedMessage.getReplyText());
                System.out.println("exchange: "+returnedMessage.getExchange());
                System.out.println("routingKey: "+returnedMessage.getRoutingKey());
                //对消息的二次处理
            }
        });
        //4.发送消息 - RoutingKey错误，将会失败
        rabbitTemplate.convertAndSend("Return-direct-exchange","wyh.return.confirm","iWyh2-Return");
    }

    @Test
    public void TTL() {
        MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setExpiration("2000");
                return message;
            }
        };
        for (int i = 0; i < 10; i++) {
            if (i == 0) {
                rabbitTemplate.convertAndSend("","","",messagePostProcessor);
            }
            rabbitTemplate.convertAndSend("bootTTLTopicExchange","wyh.ttl.error","iWyh2-TTL-error");
        }
    }

    @Test
    public void DeadMessage() {
        for (int i = 0; i < 15; i++) {
            rabbitTemplate.convertAndSend("NormalExchange","wyh.normal.info","iWyh2-normal-info");
        }
    }

    @Test
    public void DelayMessage() {
        //模拟下单成功
        System.out.println("You have successfully placed your order");
        //模拟三十分钟内用户任未支付
        //发送确认订单状态消息
        rabbitTemplate.convertAndSend("ServiceNormalExchange","wyh.service.ack","Whether the order is paid or not?");
    }
}
