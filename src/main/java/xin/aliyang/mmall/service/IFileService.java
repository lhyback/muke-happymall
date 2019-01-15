package xin.aliyang.mmall.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by lhy on 2019/1/15.
 * 文件服务虽不是功能模块, 但是应该独立为一个Service, 供controller调用
 */
public interface IFileService {

	/**
	 * 上传文件到ftp server, 上传成功返回文件名，上传失败抛出IOException
	 * @param file
	 * @param tmpPath
	 * @param remotePath
	 * @return
	 * @throws IOException
	 */
	String uploadToFtpServer(MultipartFile file, String tmpPath, String remotePath) throws IOException;
}
