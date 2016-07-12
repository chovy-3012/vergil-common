package io.vergil.common.web.exception;

/**
 * 验证异常类
 * 
 * @author zhaowei
 * @date 2015年9月29日下午9:02:26
 */
public class ValidatorException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ValidatorException() {
		super();
	}

	public ValidatorException(String message) {
		super(message);
	}

	public ValidatorException(Throwable throwable) {
		super(throwable);
	}

	public ValidatorException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
