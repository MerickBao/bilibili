package com.example.bilibili.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * @Author: merickbao
 * @Created_Time: 2022-06-15 10:07
 * @Description: 数据库映射
 */

@Mapper
public interface DemoDao {

	public Map<String, Object> query(Long id);

	public String getUserName(Long id);
}
