package io.vergil.common.lang.exception;

/**
 * 业务异常类
 * 
 * @author zhaowei
 * @date 2015年9月10日下午4:30:33
 */
public class BusinessException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public BusinessException() {
		super();
	}

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(Throwable throwable) {
		super(throwable);
	}

	public BusinessException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
