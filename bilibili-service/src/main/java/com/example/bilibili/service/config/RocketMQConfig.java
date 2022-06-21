package com.example.bilibili.service.config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.bilibili.domain.UserFollowing;
import com.example.bilibili.domain.UserMoment;
import com.example.bilibili.domain.constant.UserMomentsConstant;
import com.example.bilibili.service.UserFollowingService;
import com.mysql.cj.util.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: merickbao
 * @Created_Time: 2022-06-21 09:56
 * @Description: RocketMQ 配置类
 */

@Configuration
public class RocketMQConfig {

	@Value("${rocketmq.name.server.address}")
	private String nameServerAddr;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Autowired
	private UserFollowingService userFollowingService;

	// 生产者
	@Bean("momentsProducer")
	public DefaultMQProducer momentsProducer() throws MQClientException {
		// 设置生产消息的分组
		DefaultMQProducer producer = new DefaultMQProducer(UserMomentsConstant.GROUP_MOMENTS);
		// 设置服务器地址
		producer.setNamesrvAddr(nameServerAddr);
		producer.start();
		return producer;
	}

	// 消费者
	@Bean("momentsConsumer")
	public DefaultMQPushConsumer momentsConsumer() throws MQClientException {
		// 设置消费信息的分组
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(UserMomentsConstant.GROUP_MOMENTS);
		consumer.setNamesrvAddr(nameServerAddr);
		// 设置订阅的话题和子话题（* 表示该话题下的所有子话题）
		consumer.subscribe(UserMomentsConstant.TOPIC_MOMENTS, "*");
		// 配置监听器
		consumer.registerMessageListener(new MessageListenerConcurrently() {
			@Override
			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
				// 具体的消息消费过程
				MessageExt msg = msgs.get(0);
				if (msg == null) {
					return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
				}
				// 将序列化数据反序列化为对象
				String bodyStr = new String(msg.getBody());
				UserMoment userMoment = JSONObject.toJavaObject(JSONObject.parseObject(bodyStr), UserMoment.class);

				// 该用户的粉丝列表
				Long userId = userMoment.getUserId();
				List<UserFollowing> fanList = userFollowingService.getUserFans(userId);

				// 将用户动态存入Redis
				for (UserFollowing fan : fanList) {
					// redis中的键值：粉丝可以根据键值查询是否有自己对应的推送
					String Key = "subscribed-" + fan.getUserId();

					// 获取当前redis中该键值对应的动态列表
					String subscribedListStr = redisTemplate.opsForValue().get(Key);
					List<UserMoment> subscribedList;
					if (StringUtils.isNullOrEmpty(subscribedListStr)) {
						subscribedList = new ArrayList<>();
					} else {
						subscribedList = JSONArray.parseArray(subscribedListStr, UserMoment.class);
					}
					// 将当前动态加入到粉丝的通知列表
					subscribedList.add(userMoment);
					// 将数据写入redis
					redisTemplate.opsForValue().set(Key, JSONObject.toJSONString(subscribedList));
				}
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		});
		consumer.start();
		return consumer;
	}
}
