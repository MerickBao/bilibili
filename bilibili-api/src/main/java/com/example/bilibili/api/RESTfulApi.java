package com.example.bilibili.api;

import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @Author: merickbao
 * @Created_Time: 2022-06-15 14:45
 * @Description:
 */

@RestController
public class RESTfulApi {

	private final Map<Integer, Map<String, Object>> dataMap;

	public RESTfulApi() {
		dataMap = new HashMap<>();
		for (int i = 1; i < 3; i++) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", i);
			map.put("name", "name" + i);
			dataMap.put(i, map);
		}
	}

	@GetMapping("/objects/{id}")
	public Map<String, Object> getData(@PathVariable Integer id) {
		return dataMap.get(id);
	}

	@DeleteMapping("/objects/{id}")
	public String deleteData(@PathVariable Integer id) {
		dataMap.remove(id);
		return "delete success";
	}

	@PostMapping("/objects")
	public String postData(@RequestBody Map<String, Object> data) {
		int nextId = dataMap.keySet().stream().mapToInt(i -> i).max().getAsInt() + 1;
		dataMap.put(nextId, data);
		return "post success";
	}

	@PutMapping("/objects")
	public String putData(@RequestBody Map<String, Object> data) {
		Integer id = Integer.parseInt(String.valueOf(data.get("id")));
		Map<String, Object> containedData = dataMap.get(id);
		if (containedData == null) {
			// 不存在就新建
			int nextId = dataMap.keySet().stream().mapToInt(i -> i).max().getAsInt() + 1;
			dataMap.put(nextId, data);
		} else {
			// 存在就更新
			dataMap.put(id, data);
		}
		return "put success";
	}
}
