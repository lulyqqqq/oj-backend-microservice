package com.ac.ojbackendjudgeservice.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: InitMQ
 * @author: mafangnian
 * @date: 2023/10/23 21:38
 * 全局执行一次
 */
@Slf4j
public class InitMQ {
    public static void doInit() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("139.159.242.146");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            //交换机名称
            String EXCHANGE_NAME = "code_exchange";
            //路由方式
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");

            // 创建队列，随机分配一个队列名称
            String queueName = "code_queue";
            channel.queueDeclare(queueName, true, false, false, null);
            channel.queueBind(queueName, EXCHANGE_NAME, "my_routingKey");
            log.info("消息队列启动成功");
        } catch (Exception e) {
            log.error("消息队列启动失败");
        }
    }

    public static void main(String[] args) {
        doInit();
    }
}
