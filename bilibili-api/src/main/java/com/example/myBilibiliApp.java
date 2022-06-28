package com.example;

import com.example.bilibili.service.websocket.WebSocketService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Author: merickbao
 * @Created_Time: 2022-06-15 09:24
 * @Description: 项目启动类
 */
@SpringBootApplication
@EnableTransactionManagement
@EnableAsync
@EnableScheduling
public class myBilibiliApp {

	public static void main(String[] args) {
		ApplicationContext app = SpringApplication.run(myBilibiliApp.class, args);
		WebSocketService.setApplicationContext(app);
	}
}
