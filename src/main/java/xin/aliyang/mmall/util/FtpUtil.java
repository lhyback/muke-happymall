package xin.aliyang.mmall.util;

import com.google.common.collect.Lists;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by lhy on 2019/1/15.
 */
public class FtpUtil {
	public static final String ftpIp = PropertiesUtil.getProperty("ftp.server.ip");
	public static final String ftpUser = PropertiesUtil.getProperty("ftp.user");
	public static final String ftpPassword = PropertiesUtil.getProperty("ftp.pass");
	public static final String ftpServerHttpPrefix = PropertiesUtil.getProperty("ftp.server.http.prefix");

	private static FTPClient ftpClient = null;
	private static Logger logger = LoggerFactory.getLogger(FtpUtil.class);

	/**
	 *
	 * @param remotePath 在服务器ftp目录下的子目录
	 * @param files
	 * @return
	 */
	public static boolean uploadFile(String remotePath, List<File> files) {
		boolean isUploaded = true;
		FileInputStream fis = null;
		try {
			//1. 连接ftp server
			if (!connectServer()) {
				return false;
			}

			//2. 配置ftpClient参数
			ftpClient.changeWorkingDirectory(remotePath);  //remotePath目录必须存在且有权写入
			ftpClient.setBufferSize(1024);
			ftpClient.setControlEncoding("UTF-8");
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);  //传输二进制文件
			ftpClient.enterLocalPassiveMode();  //切换ftp协议被动模式：告知服务器开启一个端口

			//3. 传文件
			for (File file : files) {
				fis = new FileInputStream(file);
				isUploaded = ftpClient.storeFile(file.getName(), fis) && isUploaded;
			}
			fis.close();

		} catch (IOException e) {
			logger.error("上传文件到ftp server失败", e);
			isUploaded = false;
		}
		return isUploaded;
	}

	private static boolean connectServer() {
		boolean isConnected = false;
		if (ftpClient == null) {
			synchronized (FtpUtil.class) {
				if (ftpClient == null) {
					ftpClient = new FTPClient();
				}
			}

		}
		try {
			ftpClient.connect(ftpIp);
			isConnected = ftpClient.login(ftpUser, ftpPassword);
			logger.info("连接ftp服务器结果: {}", isConnected);
		} catch (IOException e) {
			logger.error("连接ftp服务器异常", e);
		}
		return isConnected;
	}

	public static void main(String[] args) {
		File file = new File("/Users/lhy/linhai.jpg");
		boolean isUploaded = uploadFile("test", Lists.newArrayList(file));
	}

}
