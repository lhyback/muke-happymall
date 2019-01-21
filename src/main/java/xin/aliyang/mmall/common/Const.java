package xin.aliyang.mmall.common;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Created by lhy on 2018/8/14.
 */
public class Const {
	public static final String CURRENT_USER = "current_user";
	public static final String EMAIL = "email";
	public static final String USERNAME = "username";

	public interface Role {
		int ROLE_CUSTOMER = 0;  //默认为public static final 变量
		int ROLE_ADMIN = 1;
	}

	public interface Cart {
		int UN_CHECKED = 0;
		int CHECKED = 1;
		String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";
		String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";
	}

	public enum ProductSaleStatus {
		ON_SALE(1, "已上架"),
		OFF_SALE(2, "已下架"),
		DELETED(3, "已删除");

		private final Integer code;
		private final String desc;

		ProductSaleStatus(Integer code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		public Integer getCode() {
			return code;
		}

		public String getDesc() {
			return desc;
		}
	}

	public static final  Set<String> SORT_SET = Sets.newHashSet("price_asc", "price_desc");
}
