package io.vergil.common.lang;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description 字符串工具
 * @ClassName StringUtil
 * @author zhaowei
 * @date 2015年7月2日下午3:34:33
 */
public class StringUtils {
	/**
	 * 解析ip的正则表达式
	 */
	private static final Pattern ipPattern = Pattern.compile("(\\d{1,3}\\.){3}(\\d{1,3})");
	private static final Pattern ipPortPattern = Pattern.compile("(\\d{1,3}.){3}(\\d{1,3}):\\d{1,5}");
	private static final Pattern ipPattern1 = Pattern.compile(
			"^((25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\\.){1,3}(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])?$");
	/**
	 * 解析邮箱的正则表达式
	 */
	private static final String EMAIL_PATTERN = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";

	/**
	 * 判断字符串是为null或空
	 * 
	 * @author zhaowei
	 * @date 2015年9月16日下午2:58:13
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		return org.apache.commons.lang3.StringUtils.isEmpty(str);
	}

	/**
	 * 判断字符串不为null或空
	 * 
	 * @author zhaowei
	 * @date 2015年9月16日下午2:58:38
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str) {
		return org.apache.commons.lang3.StringUtils.isNotEmpty(str);
	}

	/**
	 * 判断字符串不为null或全空格
	 * 
	 * @author zhaowei
	 * @date 2015年9月16日下午2:59:09
	 * @param str
	 * @return
	 */
	public static boolean isNotBlank(String str) {
		return org.apache.commons.lang3.StringUtils.isNotBlank(str);
	}

	/**
	 * 判断字符串为null或全空格
	 * 
	 * @author zhaowei
	 * @date 2015年9月16日下午2:59:09
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str) {
		return org.apache.commons.lang3.StringUtils.isBlank(str);
	}

	/**
	 * 从字符串中解析ip
	 * 
	 * @param str
	 * @return
	 * @Author zhaowei
	 * @date 2015年9月18日上午10:05:45
	 */
	public static String getIP(String str) {
		Matcher match = ipPattern.matcher(str);
		if (match.find()) {
			return match.group();
		} else {
			return null;
		}
	}

	public static boolean isIP(String str) {
		Matcher matcher = ipPattern1.matcher(str);
		if (!matcher.find()) {
			return false;
		}
		return true;
	}

	/**
	 * 生成http的url
	 * 
	 * @author zhaowei
	 * @date 2015年9月21日下午1:36:26
	 * @param ip
	 * @param port
	 * @return
	 */
	public static String buildHttpURL(String ip, int port) {
		String text = "http://{0}:{1}";
		text = MessageFormat.format(text, ip, String.valueOf(port));
		return text;
	}

	/**
	 * @author zhaowei
	 * @date 2015年9月29日下午9:35:01
	 * @param emailAddress
	 * @return
	 */
	public static boolean isEMailAddress(String emailAddress) {
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(emailAddress);
		return matcher.matches();
	}

	/**
	 * 解析出ip端口
	 * 
	 * @author zhaowei
	 * @date 2015年10月3日下午5:22:56
	 * @param address
	 * @return
	 */
	public static String getIPPort(String address) {
		Matcher match = ipPortPattern.matcher(address);
		if (match.find()) {
			return match.group();
		} else {
			return null;
		}
	}
}
