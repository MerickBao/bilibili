package com.example.bilibili.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @Author: merickbao
 * @Created_Time: 2022-06-27 20:07
 * @Description: WebSocketConfig配置类
 */

@Configuration
public class WebSocketConfig {

	// 用来发现wobsocket服务
	@Bean
	public ServerEndpointExporter serverEndpointExporter() {
		return new ServerEndpointExporter();
	}
}
