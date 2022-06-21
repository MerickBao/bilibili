package com.example.bilibili.service.util;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.CountDownLatch2;
import org.apache.rocketmq.common.message.Message;

import java.util.concurrent.TimeUnit;


/**
 * @Author: merickbao
 * @Created_Time: 2022-06-21 10:27
 * @Description: RocketMQ 工具类
 */

public class RocketMQUtil {

	// 同步发送消息
	public static void syncSendMsg(DefaultMQProducer producer, Message message) throws Exception {
		SendResult result = producer.send(message);
		System.out.println(result);
	}

	// 异步发送消息
	public static void asyncSendMsg(DefaultMQProducer producer, Message message) throws Exception {
		int messageCount = 2;
		CountDownLatch2 countDownLatch = new CountDownLatch2(messageCount);
		for (int i = 0; i < messageCount; i++) {
			producer.send(message, new SendCallback() {
				@Override
				public void onSuccess(SendResult sendResult) {
					countDownLatch.countDown();
					System.out.println(sendResult.getMsgId());
				}

				@Override
				public void onException(Throwable e) {
					countDownLatch.countDown();
					System.out.println("消息发送异常！" + e);
					e.printStackTrace();
				}
			});
		}
		countDownLatch.await(5, TimeUnit.SECONDS);
	}
}
