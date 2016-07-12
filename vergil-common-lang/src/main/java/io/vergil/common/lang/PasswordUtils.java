package io.vergil.common.lang;

/**
 * 密码工具类
 * 
 * @author zhaowei
 * @date 2015年7月5日下午6:22:36
 */
public class PasswordUtils {

	/**
	 * @description 将密码加密
	 * @title encodePassword
	 * @param userId
	 * @param password
	 * @return
	 * @Author zhaowei
	 * @date 2015年7月5日下午6:22:12
	 */
	public static String encodePassword(String userId, String password) {
		String md5pwd = DigestUtils.md5DigestAsHex((password + "~!@" + userId + "#$%").getBytes());
		return md5pwd;
	}
}
