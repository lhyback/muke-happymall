package xin.aliyang.mmall.common;

import net.sf.jsqlparser.schema.Server;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 * Created by lhy on 2018/8/14.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T> implements Serializable{

	private int status;
	private String msg;
	private T data;

	private ServerResponse(int status) {
		this.status = status;
	}

	private ServerResponse(int status, String msg) {
		this.status = status;
		this.msg = msg;
	}

	private ServerResponse(int status, T data) {
		this.status = status;
		this.data = data;
	}

	private ServerResponse(int status, String msg, T data) {
		this.status = status;
		this.data = data;
		this.msg = msg;
	}

	@JsonIgnore
	public boolean isSuccessful() {
		return status == ResponseCode.SUCCESS.getCode();
	}

	public int getStatus() {
		return status;
	}

	public String getMsg() {
		return msg;
	}

	public T getData() {
		return data;
	}

	public static <T> ServerResponse<T> createBySuccess() {
		ServerResponse response = new ServerResponse(ResponseCode.SUCCESS.getCode());
		return response;
	}

	public static <T> ServerResponse<T> createBySuccessMsg(String msg) {
		ServerResponse response = new ServerResponse(ResponseCode.SUCCESS.getCode(), msg);
		return response;
	}

	public static <T> ServerResponse<T> createBySuccessData(T data) {
		ServerResponse response = new ServerResponse(ResponseCode.SUCCESS.getCode(), data);
		return response;
	}

	public static <T> ServerResponse<T> createBySuccess(String msg, T data) {
		ServerResponse response = new ServerResponse(ResponseCode.SUCCESS.getCode(), msg, data);
		return response;
	}

	public static <T> ServerResponse<T> createByError() {
		ServerResponse response = new ServerResponse(ResponseCode.ERROR.getCode());
		return response;
	}

	public static <T> ServerResponse<T> createByErrorMsg(String msg) {
		ServerResponse response = new ServerResponse(ResponseCode.ERROR.getCode(), msg);
		return response;
	}

	public static <T> ServerResponse<T> createByErrorCodeMsg(int errorCode, String msg) {
		ServerResponse response = new ServerResponse(errorCode, msg);
		return response;
	}

}
