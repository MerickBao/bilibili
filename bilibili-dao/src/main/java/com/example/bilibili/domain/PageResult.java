package com.example.bilibili.domain;

import java.util.List;

/**
 * @Author: merickbao
 * @Created_Time: 2022-06-20 15:40
 * @Description: 分页查询相关实体类
 */

public class PageResult<T> {

	// 总个数
	private Integer total;

	// 保存结果
	private List<T> list;

	public PageResult(Integer total, List<T> list) {
		this.total = total;
		this.list = list;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}
}
