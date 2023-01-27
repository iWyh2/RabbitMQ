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
     * TTL
     * 1.Queue的统一过期时间
     * 2.消息的单独过期时间
     * 注：
     * 如果即设置了消息的过期时间，又设置了队列的统一过期时间，那么【以时间短的为准】
     * 消息过期之后，只有在队列顶端，队列才会判断这条消息是否过期该移除掉
     * （所以哪怕十条消息中有一条的存活时间短，但是它并没有在队列的顶端，是依然会存在队列中，等到队列全部过期移除掉）
     */
    @Test
    public void TTL() {
        //这十条消息将会遵从Queue的统一过期时间
        for (int i = 0; i < 10; i++) {
            rabbitTemplate.convertAndSend("TTL-exchange","wyh.ttl.info","iWyh2-TTL-info");
        }

        //消息的单独过期时间
        //1.设置消息后处理对象，设置消息的参数信息
        MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                //1.设置message的信息
                //Expiration，单位ms，当该消息在队列顶端时，会单独判断该消息是否过期
                message.getMessageProperties().setExpiration("3000");//设置消息的过期时间 - 3000ms
                //2.返回该消息
                return message;
            }
        };
        rabbitTemplate.convertAndSend("TTL-exchange","wyh.ttl.info","iWyh2-TTL-info",messagePostProcessor);
    }
}
