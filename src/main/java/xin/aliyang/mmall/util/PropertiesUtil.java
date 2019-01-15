package xin.aliyang.mmall.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by lhy on 2019/1/13.
 */
public class PropertiesUtil {
	private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
	private static Properties properties;

	static {
		properties = new Properties();
		try {
			//properties.load(PropertiesUtil.class.getResourceAsStream("mmall.properties"));
			properties.load(new InputStreamReader(PropertiesUtil.class.getClassLoader().getResourceAsStream("mmall.properties")));
		} catch (IOException e) {
			logger.error("读取配置文件失败", e);
		}
	}

	public static String getProperty(String key) {
		String value =  properties.getProperty(key.trim());
		if (StringUtils.isBlank(value)) {
			return null;
		}
		return value.trim();
	}

	public static String getProperty(String key, String defaultValue) {
		String value = properties.getProperty(key.trim());
		if (StringUtils.isBlank(value)) {
			return null;
		}
		return value.trim();
	}

}
