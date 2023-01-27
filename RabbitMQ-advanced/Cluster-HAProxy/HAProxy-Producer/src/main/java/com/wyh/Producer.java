package com.wyh;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producer {
    public static void main(String[] args) throws Exception {
        //简单模式 - 无需设置Exchange交换机
        //1. 创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //2. 设置参数
        connectionFactory.setHost("192.168.88.92");//HAProxy的ip       默认值：localhost
        connectionFactory.setPort(5672);           //HAProxy的端口     默认值：5672
        //connectionFactory.setVirtualHost("/wyh");  //虚拟机  默认值：/
        //connectionFactory.setUsername("wyh");      //用户名 默认值：guest
        //connectionFactory.setPassword("wyh");      //密码  默认值：guest
        //3. 创建Connection
        Connection connection = connectionFactory.newConnection();
        //4. 创建Channel
        Channel channel = connection.createChannel();
        //5. 创建Queue
        channel.queueDeclare("wyh-helloWorld-queue",true,false,false,null);
        //6. 发送消息到Queue
        channel.basicPublish("","wyh-helloWorld-queue",null,"WangYihuan Will Change The World!".getBytes());
        //7. 释放资源
        channel.close();
        connection.close();
        System.out.println("Send message success");
    }
}
