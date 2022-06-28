package com.example.bilibili.api;

import com.example.bilibili.api.support.UserSupport;
import com.example.bilibili.domain.Danmu;
import com.example.bilibili.domain.JsonResponse;
import com.example.bilibili.service.DanmuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: merickbao
 * @Created_Time: 2022-06-28 16:15
 * @Description:
 */

@RestController
public class DanmuApi {

	@Autowired
	private DanmuService danmuService;

	@Autowired
	private UserSupport userSupport;

	@GetMapping("/danmus")
	public JsonResponse<List<Danmu>> getDanmus(@RequestParam Long videoId,
	                                           String startTime,
	                                           String endTime) throws Exception {
		List<Danmu> list;
		try {
			//判断当前是游客模式还是用户登录模式
			userSupport.getCurrentUserId();
			//若是用户登录模式，则允许用户进行时间段筛选
			list = danmuService.getDanmus(videoId, startTime, endTime);
		} catch (Exception ignored) {
			//若为游客模式，则不允许用户进行时间段筛选
			list = danmuService.getDanmus(videoId, null, null);
		}
		return new JsonResponse<>(list);
	}

}
