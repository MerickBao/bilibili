package com.example.bilibili.service;

import com.example.bilibili.dao.FileDao;
import com.example.bilibili.domain.File;
import com.example.bilibili.service.util.FastDFSUtil;
import com.example.bilibili.service.util.MD5Util;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * @Author: merickbao
 * @Created_Time: 2022-06-24 22:48
 * @Description: 文件操作服务层
 */

@Service
public class FileService {

	@Autowired
	private FileDao fileDao;

	@Autowired
	private FastDFSUtil fastDFSUtil;

	public String uploadFileBySlices(MultipartFile slice,
	                                 String fileMD5,
	                                 Integer sliceNo,
	                                 Integer totalSliceNo) throws Exception {
		// 首先查看数据库是否有该md5
		File dbFileMD5 = fileDao.getFileByMD5(fileMD5);
		if (dbFileMD5 != null) {
			// 有的话直接返回资源对应的url
			return dbFileMD5.getUrl();
		}
		// 分片上传文件，上传成功后获得文件url
		String url = fastDFSUtil.uploadFileBySlices(slice, fileMD5, sliceNo, totalSliceNo);
		if (!StringUtil.isNullOrEmpty(url)) {
			// 最后一个分片上传成功，文件信息保存在数据库
			dbFileMD5 = new File();
			dbFileMD5.setCreateTime(new Date());
			dbFileMD5.setMd5(fileMD5);
			dbFileMD5.setUrl(url);
			dbFileMD5.setType(fastDFSUtil.getFileType(slice));
			fileDao.addFile(dbFileMD5);
		}
		// 返回url：只有最后一片分片上传成功之后才返回真的url
		return url;
	}

	public String getFileMD5(MultipartFile file) throws Exception {
		return MD5Util.getFileMD5(file);
	}

	public File getFileByMd5(String fileMd5) {
		return fileDao.getFileByMD5(fileMd5);
	}
}
