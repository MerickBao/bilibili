package com.example.bilibili.dao;

import com.example.bilibili.domain.File;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: merickbao
 * @Created_Time: 2022-06-24 22:57
 * @Description:
 */

@Mapper
public interface FileDao {

	File getFileByMD5(String md5);

	Integer addFile(File file);
}
