package xin.aliyang.mmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by lhy on 2018/8/15.
 */
public class TokenCache {

	private static final Logger logger = LoggerFactory.getLogger(TokenCache.class);
	public static final String TOKEN_PREFIX = "token_";

	private static LoadingCache<String, String> localCache = CacheBuilder.newBuilder()
			.initialCapacity(1000)
			.maximumSize(10000)
			.expireAfterAccess(30, TimeUnit.MINUTES)
			.build(new CacheLoader<String, String>() {
				@Override
				public String load(String s) throws Exception {
					//第一次加载key = s，可以去数据库取值返回
					return null;
				}
			});

	public static void setKey(String key, String value) {
		localCache.put(key, value);
	}

	public static String getKey(String key) {
		String value = null;
		try {
			value = localCache.get(key);
//			if ("null".equals(value)) {
//				return null;
//			}
//			return value;
		} catch (ExecutionException e) {
			logger.error("localCache get error", e);
		}
		return value;
	}


}
