package com.example.bilibili.service;

import com.alibaba.fastjson.JSONObject;
import com.example.bilibili.dao.UserDao;
import com.example.bilibili.domain.PageResult;
import com.example.bilibili.domain.RefreshTokenDetail;
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
import org.springframework.transaction.annotation.Transactional;

import javax.jws.soap.SOAPBinding;
import java.util.*;

/**
 * @Author: merickbao
 * @Created_Time: 2022-06-15 21:27
 * @Description: 用户相关service层方法逻辑
 */

@Service
public class UserService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private UserAuthService userAuthService;

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

		// 为用户添加默认权限角色(LV0)
		userAuthService.addUserDefaultRole(user.getId());
	}

	public User getUserByPhone(String phone) {
		return userDao.getUserByPhone(phone);
	}

	@Transactional
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

	public PageResult<UserInfo> pageListUserInfos(JSONObject params) {
		Integer no = params.getInteger("no");
		Integer size = params.getInteger("size");
		// 计算查询的起始行数
		params.put("start", (no - 1) * size);
		// 最多查询的个数
		params.put("limit", size);

		// 获取满足当前查询条件的用户有多少条
		Integer total = userDao.pageCountUserInfos(params);
		List<UserInfo> list = new ArrayList<>();
		if (total > 0) {
			list = userDao.pageListUserInfos(params);
		}
		return new PageResult<>(total, list);
	}

	public Map<String, Object> loginForDts(User user) throws Exception {
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
		Long userId = dbUser.getId();
		// 访问token
		String accessToken = TokenUtil.generateToken(userId);

		// 刷新token
		String refreshToken = TokenUtil.generateRefreshToken(userId);

		// 保存刷新token到数据库
		userDao.deleteRefreshTokenByUserId(userId);
		userDao.addRefreshToken(refreshToken, userId, new Date());

		Map<String, Object> result = new HashMap<>();
		result.put("accessToken", accessToken);
		result.put("refreshToken", refreshToken);
		return result;
	}

	public void logout(String refreshToken, Long userId) {
		userDao.deleteRefreshToken(refreshToken, userId);
	}

	public String refreshAccessToken(String refreshToken) throws Exception {
		RefreshTokenDetail refreshTokenDetail = userDao.getRefreshTokenDetail(refreshToken);
		if (refreshTokenDetail == null) {
			throw new ConditionException("555", "token过期！");
		}
		// 刷新token还在有效期内，为用户重新生成一个用户token
		Long userId = refreshTokenDetail.getUserId();
		return TokenUtil.generateToken(userId);
	}

	public List<UserInfo> batchGetUserInfoByUserIds(Set<Long> userIdList) {
		return userDao.batchGetUserInfoByUserIds(userIdList);
	}
}
