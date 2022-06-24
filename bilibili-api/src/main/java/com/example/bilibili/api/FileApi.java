package com.example.bilibili.api;

import com.example.bilibili.domain.JsonResponse;
import com.example.bilibili.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: merickbao
 * @Created_Time: 2022-06-24 22:47
 * @Description: 文件操作API
 */

@RestController
public class FileApi {

	@Autowired
	private FileService fileService;

	// 获取文件的md5值
	@PostMapping("/md5files")
	public JsonResponse<String> getFileMD5(MultipartFile file) throws Exception {
		String fileMD5 = fileService.getFileMD5(file);
		return new JsonResponse<>(fileMD5);
	}

	// 分片上传文件
	@PutMapping("/file-slices")
	public JsonResponse<String> uploadFileBySlices(MultipartFile slice,
	                                               String fileMd5,
	                                               Integer sliceNo,
	                                               Integer totalSliceNo) throws Exception {
		String filePath = fileService.uploadFileBySlices(slice, fileMd5, sliceNo, totalSliceNo);
		return new JsonResponse<>(filePath);
	}

}
