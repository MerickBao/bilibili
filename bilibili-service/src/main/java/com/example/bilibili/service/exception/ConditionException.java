package com.example.bilibili.service.exception;

/**
 * @Author: merickbao
 * @Created_Time: 2022-06-15 17:29
 * @Description:
 */

public class ConditionException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	private String code;

	public ConditionException(String code, String name) {
		super(name);
		this.code = code;
	}

	public ConditionException(String name) {
		super(name);
		code = "500"; // 服务器错误通用状态码
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
