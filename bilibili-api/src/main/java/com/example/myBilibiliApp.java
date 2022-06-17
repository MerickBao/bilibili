package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * @Author: merickbao
 * @Created_Time: 2022-06-15 09:24
 * @Description: 项目启动类
 */
@SpringBootApplication
public class myBilibiliApp {

	public static void main(String[] args) {
		ApplicationContext app = SpringApplication.run(myBilibiliApp.class, args);
	}
}
