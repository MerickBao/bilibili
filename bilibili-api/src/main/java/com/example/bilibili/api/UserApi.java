package com.example.bilibili.api;

import com.example.bilibili.api.support.UserSupport;
import com.example.bilibili.domain.JsonResponse;
import com.example.bilibili.domain.User;
import com.example.bilibili.service.UserService;
import com.example.bilibili.service.util.RSAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: merickbao
 * @Created_Time: 2022-06-15 21:28
 * @Description: 用户相关API
 */

@RestController
public class UserApi {

	@Autowired
	private UserService userService;

	@Autowired
	private UserSupport userSupport;

	// 获取RSA公钥
	@GetMapping("/rsa-pks")
	public JsonResponse<String> getRsaPublicKey() {
		String pk = RSAUtil.getPublicKeyStr();
		return new JsonResponse<>(pk);
	}

	// 获取用户信息
	@GetMapping("/users")
	public JsonResponse<User> getUserInfo() {
		Long userId = userSupport.getCurrentUserId();
		User user = userService.getUserInfo(userId);
		return new JsonResponse<>(user);
	}

	// 新建用户
	@PostMapping("/users")
	public JsonResponse<String> addUser(@RequestBody User user) {
		userService.addUser(user);
		return JsonResponse.success();
	}

	// 用户登陆: 成功后会返回用户token
	@PostMapping("/user-tokens")
	public JsonResponse<String> login(@RequestBody User user) throws Exception {
		String token = userService.login(user);
		return new JsonResponse<>(token);
	}
}
