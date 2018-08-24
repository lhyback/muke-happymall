package xin.aliyang.mmall.common;

/**
 * Created by lhy on 2018/8/14.
 */
public enum ResponseCode {
	SUCCESS(0, "成功"),
	ERROR(1, "失败"),
	NEED_LOGIN(2, "未登录"),
	ILLEGAL_ARGUMENT(3, "非法参数");

	private final int code;
	private final String desc;

	ResponseCode(int code, String desc) {
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
