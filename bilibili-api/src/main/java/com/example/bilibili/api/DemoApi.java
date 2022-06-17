package com.example.bilibili.api;

import com.example.bilibili.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Author: merickbao
 * @Created_Time: 2022-06-15 10:26
 * @Description: 控制层接口
 */

@RestController
public class DemoApi {

	@Autowired
	private DemoService demoService;

	@GetMapping("/query")
	public Map<String, Object> query(Long id) {
		return demoService.query(id);
	}

	@GetMapping("/getUserName")
	public String getUserName(Long id) {
		return demoService.getUserName(id);
	}
}
