package xin.aliyang.mmall.service.impl;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import xin.aliyang.mmall.service.IFileService;
import xin.aliyang.mmall.util.FtpUtil;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by lhy on 2019/1/15.
 */
@Service("/fileService")
public class FileService implements IFileService {

	private static Logger logger = LoggerFactory.getLogger(FileService.class);

	@Override
	public String uploadToFtpServer(MultipartFile file, String tmpPath, String remotePath) throws IOException {
		String originalFileName = file.getOriginalFilename();
		String suffix = originalFileName.substring(originalFileName.lastIndexOf('.') + 1);
		String uploadFileName = UUID.randomUUID() + "." + suffix; //返回给调用者

		logger.info("开始上传文件, originalFileName: {}, tmpPath: {}, uploadFileName: {}", originalFileName, tmpPath, uploadFileName);
		File dir = new File(tmpPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File uploadFile = new File(tmpPath, uploadFileName);

		//1. 上传到webapp下的upload文件夹
		logger.info("开始上传文件到tomcat");
		file.transferTo(uploadFile);

		//2. 上传到ftp server
		logger.info("开始上传文件到ftp server");
		boolean isUploaded = FtpUtil.uploadFile(remotePath, Lists.newArrayList(uploadFile));

		//3. 全部上传成功，删除webapp的upload中的对应文件
		if (isUploaded) {
			logger.info("上传到ftp server成功，删除tomcat中的文件");
			uploadFile.delete();
		} else {
			throw new IOException("上传文件异常");
		}
		return uploadFile.getName();
	}
}
