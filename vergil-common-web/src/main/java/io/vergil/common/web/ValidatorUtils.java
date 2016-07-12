package io.vergil.common.web;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.vergil.common.lang.StringUtils;
import io.vergil.common.web.exception.ValidatorException;

/**
 * 验证工具类
 * 
 * @author zhaowei
 * @date 2015年9月29日下午8:41:27
 */
public class ValidatorUtils {

	/**
	 * 验证表达式为true，不为true则抛出ValidatorException
	 * 
	 * @param expression
	 * @param message
	 */
	public static void isTrue(boolean expression, String message) {
		if (!expression) {
			throw new ValidatorException(message);
		}
	}

	/**
	 * 验证是否为空
	 * 
	 * @author zhaowei
	 * @date 2015年9月29日下午9:23:02
	 * @param paramter
	 * @return
	 * @throws ValidatorException
	 */
	public static boolean validatorIfEmpty(Object[] parameters, String[] tips) throws ValidatorException {
		for (int i = 0; i < parameters.length; i++) {
			Object ob = parameters[i];
			String tip = null;
			try {
				tip = tips[i];
			} catch (Exception e) {
				tip = "参数不能为空";
			}
			if (ob == null) {
				throw new ValidatorException(tip);
			} else if (ob instanceof String && StringUtils.isBlank((String) ob)) {
				throw new ValidatorException(tip);
			}
		}
		return true;
	}

	/**
	 * 验证是否为空
	 * 
	 * @author zhaowei
	 * @date 2015年9月29日下午9:23:02
	 * @param paramter
	 * @return
	 * @throws ValidatorException
	 */
	public static boolean validatorIfEmpty(Object[] paramters) throws ValidatorException {
		return validatorIfEmpty(paramters, new String[] {});
	}

	/**
	 * 验证是否为空
	 * 
	 * @author zhaowei
	 * @date 2015年9月29日下午9:23:02
	 * @param paramter
	 * @return
	 * @throws ValidatorException
	 */
	public static boolean validatorIfEmpty(Object paramter, String tip) throws ValidatorException {
		return validatorIfEmpty(new Object[] { paramter }, new String[] { tip });
	}

	/**
	 * 验证是否为空
	 * 
	 * @author zhaowei
	 * @date 2015年9月29日下午9:23:02
	 * @param paramter
	 * @return
	 * @throws ValidatorException
	 */
	public static boolean validatorIfEmpty(Object paramter) throws ValidatorException {
		return validatorIfEmpty(new Object[] { paramter }, new String[] {});
	}

	/**
	 * 验证是否是邮箱
	 * 
	 * @author zhaowei
	 * @date 2015年9月29日下午9:30:25
	 * @return
	 * @throws ValidatorException
	 */
	public static boolean validatorIfEmail(String emailAddress) throws ValidatorException {
		return validatorIfEmail(emailAddress, "邮箱格式不正确");
	}

	/**
	 * 验证是否是邮箱
	 * 
	 * @author zhaowei
	 * @date 2015年9月29日下午9:30:25
	 * @return
	 * @throws ValidatorException
	 */
	public static boolean validatorIfEmail(String emailAddress, String tip) throws ValidatorException {
		if (!StringUtils.isEMailAddress(emailAddress)) {
			throw new ValidatorException(tip);
		}
		return true;
	}

	/**
	 * 验证是否是数字
	 * 
	 * @author liming
	 * @date 2015年10月2日上午9:53:09
	 * @param emailAddress
	 * @param tip
	 * @return
	 * @throws ValidatorException
	 */
	public static boolean validatorIfNum(String num, String tip) throws ValidatorException {
		try {
			Integer.parseInt(num);
		} catch (NumberFormatException e) {
			throw new ValidatorException(tip);
		}
		return true;
	}

	public static boolean validatorIfNum(Integer num, Integer base, Integer head, String tip)
			throws ValidatorException {
		if (base != null && num <= base) {
			throw new ValidatorException(tip);
		}
		if (head != null && num >= head) {
			throw new ValidatorException(tip);
		}
		return true;
	}

	/**
	 * 集群 数据名 表名正则
	 */
	public static final String NAME_MODEL = "^(?!_)(?!.*?_$)[a-zA-Z0-9_]+$";

	/**
	 * 匹配是否为 数字 字母下划线组合,且下划线不能开头,
	 * 
	 * @author liming
	 * @date 2015年10月7日上午11:15:03
	 * @param dbName
	 * @return
	 * @throws ValidatorException
	 */
	public static boolean validatorIfDB(String dbName, String tip) throws ValidatorException {
		Pattern dbPattern = Pattern.compile(NAME_MODEL);
		Matcher matcher = dbPattern.matcher(dbName);
		if (!matcher.find()) {
			throw new ValidatorException(tip);
		}
		return true;
	}

	/**
	 * IP 地址正则表达式
	 */
	public static final String IP_MODEL = "^((25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\\.){1,3}(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])?$";

	/**
	 * 验证是否为合法的ip地址
	 * 
	 * @author liming
	 * @date 2015年10月7日上午11:20:05
	 * @param ip
	 * @return
	 * @throws ValidatorException
	 */
	public static boolean validatorIfIP(String ip, String tip) throws ValidatorException {
		Pattern dbPattern = Pattern.compile(IP_MODEL);
		Matcher matcher = dbPattern.matcher(ip);
		if (!matcher.find()) {
			throw new ValidatorException(tip);
		}
		return true;
	}
}
