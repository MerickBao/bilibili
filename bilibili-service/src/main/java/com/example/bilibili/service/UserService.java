package com.example.bilibili.service;

import com.example.bilibili.dao.UserDao;
import com.example.bilibili.domain.User;
import com.example.bilibili.domain.UserInfo;
import com.example.bilibili.domain.constant.UserConstant;
import com.example.bilibili.service.exception.ConditionException;
import com.example.bilibili.service.util.MD5Util;
import com.example.bilibili.service.util.RSAUtil;
import com.example.bilibili.service.util.TokenUtil;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.jws.soap.SOAPBinding;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @Author: merickbao
 * @Created_Time: 2022-06-15 21:27
 * @Description: 用户相关service层方法逻辑
 */

@Service
public class UserService {

	@Autowired
	private UserDao userDao;

	public void addUser(User user) {
		String phone = user.getPhone();
		if (StringUtils.isNullOrEmpty(phone)) {
			throw new ConditionException("手机号不能为空！");
		}
		User dbUser = getUserByPhone(phone);
		if (dbUser != null) {
			throw new ConditionException("该手机号已经被注册！");
		}

		Date now = new Date();
		String salt = String.valueOf(now.getTime());
		String password = user.getPassword(); // 经过前端RSA加密过后的密码
		String rawPassword; // 解密后的明文密码
		try {
			rawPassword = RSAUtil.decrypt(password);
		} catch (Exception e) {
			throw new ConditionException("密码解密失败");
		}
		// 将明文密码和盐值拼接，计算md5值
		String md5Password = MD5Util.sign(rawPassword, salt, "UTF-8");

		// 写入数据库t_user表
		user.setSalt(salt);
		user.setPassword(md5Password);
		user.setCreateTime(now);
		userDao.addUser(user);

		// 添加用户信息
		UserInfo userInfo = new UserInfo();
		userInfo.setUserId(user.getId());
		userInfo.setNickname(UserConstant.DEFAULT_NICK_NAME);
		userInfo.setBirth(UserConstant.DEFAULT_BIRTH);
		userInfo.setGender(UserConstant.GENDER_UNKNOW);
		userInfo.setCreateTime(now);
		userDao.addUserInfo(userInfo);
	}

	public User getUserByPhone(String phone) {
		return userDao.getUserByPhone(phone);
	}

	public String login(User user) throws Exception {
		String phone = user.getPhone();
		if (StringUtils.isNullOrEmpty(phone)) {
			throw new ConditionException("手机号不能为空！");
		}
		User dbUser = getUserByPhone(phone);
		if (dbUser == null) {
			throw new ConditionException("用户不存在！");
		}
		String password = user.getPassword();
		String rawPassword;
		try {
			rawPassword = RSAUtil.decrypt(password);
		} catch (Exception e) {
			throw new ConditionException("密码解密失败！");
		}
		String salt = dbUser.getSalt();
		String md5Password = MD5Util.sign(rawPassword, salt, "UTF-8");
		if (!md5Password.equals(dbUser.getPassword())) {
			throw new ConditionException("密码错误");
		}
		// 用户信息验证成功，开始生成用户token并返回
		return TokenUtil.generateToken(dbUser.getId());
	}

	public User getUserInfo(Long userId) {
		User user = userDao.getUserById(userId);
		UserInfo userInfo = userDao.getUserInfoByUserId(userId);
		user.setUserInfo(userInfo);
		return user;
	}

	public void updateUserInfos(UserInfo userInfo) {
		userInfo.setUpdateTime(new Date());
		userDao.updateUserInfos(userInfo);
	}

	public void updateUsers(User user) throws Exception {
		Long id = user.getId();
		User dbUser = userDao.getUserById(id);
		if (dbUser == null) {
			throw new ConditionException("用户不存在！");
		}
		if (!StringUtils.isNullOrEmpty(user.getPassword())) {
			String rawPassword = RSAUtil.decrypt(user.getPassword());
			String md5Password = MD5Util.sign(rawPassword, dbUser.getSalt(), "UTF-8");
			user.setPassword(md5Password);
		}
		user.setUpdateTime(new Date());
		userDao.updateUsers(user);
	}

	public User getUserById(Long userId) {
		return userDao.getUserById(userId);
	}

	public List<UserInfo> getUserInfoByUserIds(Set<Long> userIdList) {
		return userDao.getUserInfoByUserIds(userIdList);
	}
}
