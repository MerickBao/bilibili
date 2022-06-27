package com.example.bilibili.service;

import com.example.bilibili.dao.UserCoinDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Author: merickbao
 * @Created_Time: 2022-06-27 17:45
 * @Description: 用户投币服务层
 */

@Service
public class UserCoinService {

	@Autowired
	private UserCoinDao userCoinDao;

	public Integer getUserCoinsAmount(Long userId) {
		return userCoinDao.getUserCoinsAmount(userId);
	}

	public void updateUserCoinsAmount(Long userId, Integer amount) {
		Date updateTime = new Date();
		userCoinDao.updateUserCoinAmount(userId, amount, updateTime);
	}
}
