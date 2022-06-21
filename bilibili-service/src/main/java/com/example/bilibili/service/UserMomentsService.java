package com.example.bilibili.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.bilibili.dao.UserMomentsDao;
import com.example.bilibili.domain.UserMoment;
import com.example.bilibili.domain.constant.UserMomentsConstant;
import com.example.bilibili.service.util.RocketMQUtil;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

/**
 * @Author: merickbao
 * @Created_Time: 2022-06-21 10:43
 * @Description: 用户动态服务层
 */

@Service
public class UserMomentsService {

	@Autowired
	private UserMomentsDao userMomentsDao;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	public void addUserMoments(UserMoment userMoment) throws Exception {
		userMoment.setCreateTime(new Date());
		userMomentsDao.addUserMoments(userMoment);
		// 获取注册的生产者 Bean 类
		DefaultMQProducer producer = (DefaultMQProducer) applicationContext.getBean("momentsProducer");
		// 要发送的消息
		Message message = new Message(UserMomentsConstant.TOPIC_MOMENTS, JSONObject.toJSONString(userMoment).getBytes(StandardCharsets.UTF_8));
		RocketMQUtil.syncSendMsg(producer, message);
	}

	public List<UserMoment> getUserSubscribedMoments(Long userId) {
		String Key = "subscribed-" + userId;
		String listStr = redisTemplate.opsForValue().get(Key);
		return JSONArray.parseArray(listStr, UserMoment.class);
	}
}
