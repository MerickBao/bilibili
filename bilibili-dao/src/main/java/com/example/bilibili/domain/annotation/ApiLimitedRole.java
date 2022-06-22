package com.example.bilibili.domain.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @Author: merickbao
 * @Created_Time: 2022-06-22 16:02
 * @Description: 用户权限控制 --> 接口控制
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
@Component
public @interface ApiLimitedRole {

	String[] limitedRoleCodeList() default {};
}
