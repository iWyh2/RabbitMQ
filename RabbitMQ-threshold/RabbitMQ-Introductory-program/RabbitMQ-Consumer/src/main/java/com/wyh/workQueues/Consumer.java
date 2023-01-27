package com.wyh.workQueues;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * 在工作队列模式中，此类需要启动多次，作为多个消费者
 */
public class Consumer {
    public static void main(String[] args) throws Exception {
        //工作队列模式 - 无需设置Exchange交换机
        //1. 创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //2. 设置参数
        connectionFactory.setHost("192.168.88.92");//ip       默认值：localhost
        connectionFactory.setPort(5672);           //端口     默认值：5672
        connectionFactory.setVirtualHost("/wyh");  //虚拟机  默认值：/
        connectionFactory.setUsername("wyh");      //用户名 默认值：guest
        connectionFactory.setPassword("wyh");      //密码  默认值：guest
        //3. 创建Connection
        Connection connection = connectionFactory.newConnection();
        //4. 创建Channel
        Channel channel = connection.createChannel();
        //5. 从Queue接收消息
        /*
         * basicConsume方法参数：
         * 1. queue：队列名称
         * 2. autoAck：是否自动确认,消费者收到消息后自动确认
         * 3. callback：回调对象 - Consumer接口 - DefaultConsumer实现类 - 回调方法：handleDelivery
         *            -> 回调方法：handleDelivery，当接收到消息后自动执行的方法
         *               参数：
         *                  1. consumerTag：标识
         *                  2. envelope：获取一些信息，比如交换机，路由key等
         *                  3. properties：配置信息
         *                  4. body：从Queue获取的消息数据
         */
        com.rabbitmq.client.Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("body: " + new String(body));
            }
        };
        channel.basicConsume("WorkQueues",true,consumer);
        //6. 不要释放资源，消费者相当于是监听队列中是否存在消息，存在就获取来消费掉
    }
}
