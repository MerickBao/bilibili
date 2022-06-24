package com.example.bilibili.service.util;

import com.example.bilibili.service.exception.ConditionException;
import com.github.tobato.fastdfs.domain.fdfs.MetaData;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.AppendFileStorageClient;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author: merickbao
 * @Created_Time: 2022-06-24 20:39
 * @Description: FasfDFS文件服务器工具类
 */

@Component
public class FastDFSUtil {

	@Autowired
	FastFileStorageClient fastFileStorageClient;

	@Autowired
	AppendFileStorageClient appendFileStorageClient;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	private static final String DEFAULT_GROUP = "group1";

	private static final String PATH_KEY = "path-key:";

	private static final String UPLOADED_SIZE_KEY = "uploaded-size-key:";

	private static final String UPLOADED_NO_KEY = "uploaded-no-key:";

	private static final int SLICE_SIZE = 1024 * 1024 * 2; // 2M

	// 获取文件类型
	public String getFileType(MultipartFile file) {
		if (file == null) {
			throw new ConditionException("非法文件！");
		}
		String fileName = file.getOriginalFilename();
		int index = fileName.lastIndexOf(".");
		return fileName.substring(index + 1);
	}

	// 上传文件
	public String uploadCommonFile(MultipartFile file) throws Exception {
		Set<MetaData> metaDataSet = new HashSet<>();
		String fileType = this.getFileType(file);
		StorePath storePath = fastFileStorageClient.uploadFile(file.getInputStream(), file.getSize(), fileType, metaDataSet);
		return storePath.getPath();
	}

	// 上传断点续传文件
	public String uploadAppenderFile(MultipartFile file) throws Exception {
		String fileName = file.getOriginalFilename();
		String fileType = this.getFileType(file);
		StorePath storePath = appendFileStorageClient.uploadAppenderFile(DEFAULT_GROUP, file.getInputStream(), file.getSize(), fileType);
		return storePath.getPath();
	}

	// 修改断点续传文件
	public void modifyAppenderFile(MultipartFile file, String filePath, long offset) throws Exception {
		appendFileStorageClient.modifyFile(DEFAULT_GROUP, filePath, file.getInputStream(), file.getSize(), offset);
	}

	// 上传分片文件
	public String uploadFileBySlices(MultipartFile file, String fileMd5, Integer sliceNo, Integer totalSliceNo) throws Exception {
		if (file == null || sliceNo == null || totalSliceNo == null) {
			throw new ConditionException("参数异常！");
		}
		// redis键值
		String pathKey = PATH_KEY + fileMd5;
		String uploadedSizeKey = UPLOADED_SIZE_KEY + fileMd5;
		String uploadedNoKey = UPLOADED_NO_KEY + fileMd5;

		// 获取已经上传的文件大小
		String uploadedSizeStr = redisTemplate.opsForValue().get(uploadedSizeKey);
		Long uploadedSize = 0L;
		if (!StringUtils.isNullOrEmpty(uploadedSizeStr)) {
			uploadedSize = Long.valueOf(uploadedSizeStr);
		}

		String fileType = this.getFileType(file);
		if (sliceNo == 1) { // 上传的是第一个分片
			String path = this.uploadAppenderFile(file);
			if (StringUtils.isNullOrEmpty(path)) {
				throw new ConditionException("上传失败！");
			}
			redisTemplate.opsForValue().set(pathKey, path);

			// 记录已经上传成功的分片号
			redisTemplate.opsForValue().set(uploadedNoKey, "1");
		} else {
			String filePath = redisTemplate.opsForValue().get(pathKey);
			if (StringUtils.isNullOrEmpty(filePath)) {
				throw new ConditionException("上传失败！");
			}
			// 修改指定的分片
			this.modifyAppenderFile(file, filePath, uploadedSize);
			// 已上传的分片号加一
			redisTemplate.opsForValue().increment(uploadedNoKey);
		}
		// 更新已经上传的文件大小
		uploadedSize += file.getSize();
		redisTemplate.opsForValue().set(uploadedSizeKey, String.valueOf(uploadedSize));

		// 如果所有分片上传完毕，情况redis里边相关的key和value
		String uploadedNoStr = redisTemplate.opsForValue().get(uploadedNoKey);
		Integer uploadedNo = Integer.valueOf(uploadedNoStr);
		String resultPath = "";
		if (uploadedNo.equals(totalSliceNo)) {
			// 删除pathKey之前，先保存之前的文件上传路径
			resultPath = redisTemplate.opsForValue().get(pathKey);
			List<String> keyList = Arrays.asList(uploadedNoKey, pathKey, uploadedSizeKey);
			redisTemplate.delete(keyList);
		}
		return resultPath;
	}

	// 将文件分片
	public void convertFileToSlices(MultipartFile multipartFile) throws Exception {
		String fileName = multipartFile.getOriginalFilename();
		String fileType = this.getFileType(multipartFile);
		File file = this.multipartFileToFile(multipartFile);
		long fileLength = file.length();
		int count = 1;
		for (int i = 0; i < fileLength; i += SLICE_SIZE) {
			RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
			// 找到文件中当前长度对应的位置
			randomAccessFile.seek(i);
			// 从该位置开始，读取SLICE_SIZE大小的文件内容到bytes数组
			byte[] bytes = new byte[SLICE_SIZE];
			// 返回实际读取到bytes中的文件大小
			int len = randomAccessFile.read(bytes);
			// 临时文件的存储路径
			String path = "/Users/merickbao/dev/tmpfile/" + count + "." + fileType;
			// 将文件写入指定的路径
			File slice = new File(path);
			FileOutputStream fos = new FileOutputStream(slice);
			fos.write(bytes, 0, len);
			fos.close();
			randomAccessFile.close();
			count++;
		}
		file.delete();
	}

	// 将MultipartFile转为File类型
	public File multipartFileToFile(MultipartFile multipartFile) throws Exception {
		String originalFilename = multipartFile.getOriginalFilename();
		String[] fileName = originalFilename.split("\\.");
		// 用来接收的临时文件
		File file = File.createTempFile(fileName[0], "." + fileName[1]);
		multipartFile.transferTo(file);
		return file;
	}

	// 删除文件
	public void deleteFile(String filePath) {
		fastFileStorageClient.deleteFile(filePath);
	}
}
