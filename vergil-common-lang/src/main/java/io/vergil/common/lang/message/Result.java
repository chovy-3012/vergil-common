package io.vergil.common.lang.message;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * @Description json返回结果格式
 * @ClassName Result
 * @author zhaowei
 * @date 2015年9月8日下午8:49:56
 */
public class Result {
	public enum STATUS {
		OK, ERROR
	}

	public enum CODE {
		OK(200), // 正常
		ERROR(400), // 错误
		NOT_FOUND(404), // 找不到资源
		NO_PERMISSION(405), // 没有权限
		SESSION_TIMEOUT(406);// 会话超时

		private Integer value;

		private CODE(Integer value) {
			this.value = value;
		}

		public Integer getValue() {
			return value;
		}

		public void setValue(Integer value) {
			this.value = value;
		}

	}

	private STATUS status;
	private Integer code;
	private String message;
	private Object result;

	public Result() {
		super();
	}

	public Result(STATUS status, Integer code, String message, Object result) {
		super();
		this.status = status;
		this.code = code;
		this.message = message;
		this.result = result;
	}

	public Result(Integer code, String message, Object result) {
		super();
		if (code != 200) {
			this.status = STATUS.ERROR;
		} else {
			this.status = STATUS.OK;
		}

		this.code = code;
		this.message = message;
		this.result = result;
	}

	public static String result(STATUS status, Integer code, String message, Object result) {
		return new Result(status, code, message, result).toString();
	}

	public static String error(String message, Object result) {
		return new Result(STATUS.ERROR, CODE.ERROR.getValue(), message, result).toString();
	}

	public static String error(String message) {
		return new Result(STATUS.ERROR, CODE.ERROR.getValue(), message, null).toString();
	}

	public static String error() {
		return new Result(STATUS.ERROR, CODE.ERROR.getValue(), "", null).toString();
	}

	public static String ok(String message, Object result) {
		return new Result(STATUS.OK, CODE.OK.getValue(), message, result).toString();
	}

	public static String ok(Object result) {
		return new Result(STATUS.OK, CODE.OK.getValue(), "", result).toString();
	}

	public static String ok() {
		return new Result(STATUS.OK, CODE.OK.getValue(), "", null).toString();
	}

	public static String lackOfParam() {
		return new Result(STATUS.ERROR, CODE.ERROR.getValue(), "缺少参数", null).toString();
	}

	public STATUS getStatus() {
		return status;
	}

	public void setStatus(STATUS status) {
		this.status = status;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Integer getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	@Override
	public String toString() {
		result = result == null ? "" : result;
		message = message == null ? "" : message;
		String str = JSONObject.toJSONString(this, SerializerFeature.WriteNullNumberAsZero,
				SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteMapNullValue,
				SerializerFeature.WriteNullBooleanAsFalse, SerializerFeature.WriteNullListAsEmpty);
		str = str.replaceAll("\t", "");
		return str;
	}

}
