package com.ac.ojbackendjudgeservice;

import com.ac.ojbackendjudgeservice.rabbitmq.InitMQ;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.ac")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.ac.ojbackendserviceclient.service"})
public class OjBackendJudgeServiceApplication {

	public static void main(String[] args) {
		// 初始化消息队列
		InitMQ.doInit();
		SpringApplication.run(OjBackendJudgeServiceApplication.class, args);
	}

}
