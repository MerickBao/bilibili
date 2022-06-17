package com.example.bilibili.service;

import com.example.bilibili.dao.DemoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author: merickbao
 * @Created_Time: 2022-06-15 10:15
 * @Description: demo中间层
 */

@Service
public class DemoService {

    @Autowired
    private DemoDao demoDao;

    public Map<String, Object> query(Long id) {
        return demoDao.query(id);
    }

    public String getUserName(Long id) {
        return demoDao.getUserName(id);
    }
}
