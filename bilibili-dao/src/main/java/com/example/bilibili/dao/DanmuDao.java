package com.example.bilibili.dao;
import com.example.bilibili.domain.Danmu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author: merickbao
 * @Created_Time: 2022-06-28 15:41
 * @Description:
 */

@Mapper
public interface DanmuDao {

    Integer addDanmu(Danmu danmu);

    List<Danmu> getDanmus(Map<String,Object> params);
}
